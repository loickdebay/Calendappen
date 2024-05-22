package fr.but3.saeweb.controlers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.but3.saeweb.entities.Client;
import fr.but3.saeweb.others.AllProperties;
import fr.but3.saeweb.others.CreationClientRules;
import fr.but3.saeweb.others.Md5;
import fr.but3.saeweb.others.ModificationClientRules;
import fr.but3.saeweb.others.Principal;
import fr.but3.saeweb.repositories.ClientRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;


@Controller
class controlerCheckAuthLogin {

    @Autowired
    ClientRepo clientRepo;
    @Autowired
    AllProperties allProperties;
    @Transactional
    @GetMapping("/login")
    public String checkUserIsConnected(HttpSession session){
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal != null){
            return "redirect:/calendrier";
        }else{
            return "login";
        }
    }

    @PostMapping("/login")
    public String checkUser(String email,String password,HttpServletRequest request,ModelMap modelmap){
        String md5password = Md5.doMd5Hash(password);
        Client getedClient = clientRepo.findByEmailAndPassword(email.trim(), md5password);
        if(getedClient == null){
            modelmap.addAttribute("warning", "Information incorrectes");
            return "login";
        }

        //CREATE PRINCIPAL

        Principal principal = new Principal();
        principal.setLogin(getedClient.getEmail());

     
        if(getedClient.getRole().equals("admin")){
            principal.setAdmin(true);
        }else{
            principal.setAdmin(false);
        }

        principal.setWorkday(allProperties.getWorkday());
        principal.setClient(getedClient);

        //SESSION

        HttpSession session = request.getSession();
        session.setAttribute("principal", principal);
        
        return "redirect:/calendrier";
    }

    @GetMapping("/disconnect")
    public String disconnect(HttpSession session){
        session.invalidate();
        return "login";
    }

    @GetMapping("/createAnAccount")
    public String createAccount(){
        return "newAccount";
    }
    
    @PostMapping("/createAnAccount")
    public String createUser(@Validated(CreationClientRules.class) @ModelAttribute Client user,BindingResult result, HttpServletRequest request,ModelMap modelmap) {
        Client getedClient = clientRepo.findByEmail(user.getEmail());
        if(result.hasErrors()){
            modelmap.addAttribute("errors",result.getAllErrors());
            modelmap.addAttribute("WarningAccount", "Les informations sont invalides");
            return "newAccount";
        }
        else if(getedClient == null){
            user.setName(user.getName().trim());
            user.setFirstName(user.getFirstName().trim());
            user.setPhoneNumber(user.getPhoneNumber().trim());
            user.setEmail(user.getEmail().trim());
            user.setPassword(Md5.doMd5Hash(user.getPassword()));
            clientRepo.save(user);
        }
        else{
            modelmap.addAttribute("WarningAccount", "Un compte existe deja !");
            return "newAccount";
        }

        //CREATE PRINCIPAL
        Client newClient = clientRepo.findByEmail(user.getEmail());
        Principal principal = new Principal();
        principal.setLogin(user.getEmail());
        principal.setAdmin(false);
        principal.setWorkday(allProperties.getWorkday());
        principal.setClient(newClient);
        
        HttpSession session = request.getSession();
        session.setAttribute("principal", principal);

        return "redirect:/calendrier";
    }

    @GetMapping("/editAccount")
    public String editUser(HttpSession session,ModelMap modelMap){
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        Client getClient = clientRepo.findByEmail(principal.getLogin());
        modelMap.addAttribute("client", getClient);
        return "info";
    }

    @Transactional
    @PostMapping("/editAccount")
    public String editUser(@Validated(ModificationClientRules.class) @ModelAttribute Client user,BindingResult result,ModelMap modelMap,RedirectAttributes redirectAttributes,HttpSession session){
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
                
        Client getedClient = clientRepo.findByEmail(user.getEmail());
        
        
        
        if(getedClient != null && !(principal.getLogin().equals(getedClient.getEmail()))){
            
            modelMap.addAttribute("WarningAccount", "L'email existe déjà");

            Client actualClientInfo = clientRepo.findByEmail(principal.getLogin());
            Client modelClient = new Client();
            modelClient.setName(actualClientInfo.getName());
            modelClient.setFirstName(actualClientInfo.getFirstName());
            modelClient.setPhoneNumber(actualClientInfo.getPhoneNumber());
            modelClient.setEmail(actualClientInfo.getEmail());
            modelClient.setFileName(actualClientInfo.getFileName());
            modelMap.addAttribute("client",modelClient);

            return "info";
        }

        if(result.hasErrors()){
            Client actualClientInfo = clientRepo.findByEmail(principal.getLogin());
            Client modelClient = new Client();
            modelClient.setName(actualClientInfo.getName());
            modelClient.setFirstName(actualClientInfo.getFirstName());
            modelClient.setPhoneNumber(actualClientInfo.getPhoneNumber());
            modelClient.setEmail(actualClientInfo.getEmail());
            modelClient.setFileName(actualClientInfo.getFileName());
            modelMap.addAttribute("client",modelClient);
            modelMap.addAttribute("WarningAccount", "Les informations modifiées sont invalides");
            return "info";
        }

        boolean passwordChanged = false;
        boolean emailChanged = false;
        boolean dataChanged = false;


        // Forest If to compare

        Client getedClientToCompare = clientRepo.findByEmail(principal.getLogin());
        

        if(!user.getName().equals(getedClientToCompare.getName()) && !user.getName().isBlank() ){
            getedClientToCompare.setName(user.getName());
            dataChanged = true;
        }

        if(!user.getFirstName().equals(getedClientToCompare.getFirstName()) && !user.getFirstName().isBlank()){
            getedClientToCompare.setFirstName(user.getFirstName());
            dataChanged = true;
        }

        if(!user.getPhoneNumber().trim().equals(getedClientToCompare.getPhoneNumber().trim()) && !user.getPhoneNumber().isBlank()){
            getedClientToCompare.setPhoneNumber(user.getPhoneNumber().trim());
            dataChanged = true;
        }
        
        
        
        if(!user.getEmail().equals(getedClientToCompare.getEmail()) && !user.getEmail().isBlank()){
            getedClientToCompare.setEmail(user.getEmail());
            dataChanged = true;
            emailChanged = true;
        }
        


        if(!user.getPassword().isBlank()){
            user.setPassword(Md5.doMd5Hash(user.getPassword()));
            if(!user.getPassword().equals(getedClientToCompare.getPassword())){
                getedClientToCompare.setPassword(user.getPassword());
                dataChanged = true;
                passwordChanged = true;
            }
        }

        if(dataChanged){
            clientRepo.save(getedClientToCompare);
            modelMap.addAttribute("SuccessAccount","Informations modifiées avec succès");
        }
        
        if(dataChanged == false){
            modelMap.addAttribute("NothingAppend","Aucunes informations modifiées");
        }
        
        if(passwordChanged == true || emailChanged == true){
            //DESTROY SESSION
            session.invalidate();
            return "redirect:/login";
        }

        Client modelClient = new Client();
        modelClient.setName(getedClientToCompare.getName());
        modelClient.setFirstName(getedClientToCompare.getFirstName());
        modelClient.setPhoneNumber(getedClientToCompare.getPhoneNumber());
        modelClient.setEmail(getedClientToCompare.getEmail());
        modelClient.setFileName(getedClientToCompare.getFileName());
        modelMap.addAttribute("client",modelClient);
        return "info";
    }
}
