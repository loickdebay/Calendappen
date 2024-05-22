package fr.but3.saeweb.controlers;

import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

import fr.but3.saeweb.others.Principal;
import fr.but3.saeweb.repositories.ClientRepo;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import java.nio.file.Path;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import fr.but3.saeweb.entities.Client;


@Controller
public class UploadController {

    @Autowired ClientRepo clientRepo;

    @GetMapping("/upload")
    public String showUploadForm(HttpSession session,ModelMap modelmap) {
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        String login = principal.getLogin();
        Client client = clientRepo.findByEmail(login);
        String requestImg = "/download/"+client.getFileName();
        modelmap.addAttribute("requestImg",requestImg);
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,HttpSession session) {
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        String login = principal.getLogin();

        Client client = clientRepo.findByEmail(login);
        
        if (!file.isEmpty()) {
            try {
                String workingDir = System.getProperty("user.dir");
                String uploadPath = workingDir + "/upload";
                File uploadDirectory = new File(uploadPath);
                
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                String originalFilename = file.getOriginalFilename();
                String fileExtension = Files.getFileExtension(originalFilename);
                file.transferTo(new File(uploadPath + "/" + login+"."+fileExtension));
                client.setFileName(login+"."+fileExtension);
                clientRepo.save(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/upload";
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        String workingDir = System.getProperty("user.dir");
        String uploadPath = workingDir + "/upload";
        Path filePath = Paths.get(uploadPath).resolve(fileName);

        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                String defaultFilePath = workingDir + "/upload/avatar-no-image.png";
                Resource defaultResource = new UrlResource(Paths.get(defaultFilePath).toUri());

                
                if (!defaultResource.exists()) {
                    return ResponseEntity.notFound().build();
                }

                
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + defaultResource.getFilename() + "\"")
                        .body(defaultResource);
            }

            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
