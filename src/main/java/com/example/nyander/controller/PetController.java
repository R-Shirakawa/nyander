package com.example.nyander.controller;

import com.example.nyander.controller.form.ChatGroupForm;
import com.example.nyander.controller.form.PetForm;
import com.example.nyander.repository.PetRepository;
import com.example.nyander.repository.entity.ChatGroup;
import com.example.nyander.repository.entity.Pet;
import com.example.nyander.repository.entity.User;
import com.example.nyander.service.ChatService;
import com.example.nyander.service.PetService;
import lombok.SneakyThrows;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpSession;

import static com.example.nyander.security.SecurityConfig.getLoginUser;

@Controller
public class PetController {
    @Autowired
    PetService petService;

    @Autowired
    PetRepository petRepository;

    @Autowired
    HttpSession session;

    @Autowired
    ChatService chatService;


    /*
     * ペット詳細画面表示
     */
    @GetMapping("/petDetail")
    public ModelAndView petDetail(@RequestParam(name = "petId", required = false) String petId, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();

        //ログインしているときのみログインユーザーのIDを渡す
        if (getLoginUser() != null) {
            Integer loginId = getLoginUser().getId();
            mav.addObject("loginId", loginId);
        }

        List<ChatGroupForm> chatGroupFormList = chatService.findAllChatGroup();

        try {
            if (ObjectUtils.isEmpty(petId)) {
                throw new ObjectNotFoundException(petId, "Pet");
            }
            List<PetForm> pets = petService.findById(Integer.parseInt(petId));
            if (pets == null) {
                throw new ObjectNotFoundException(petId, "Pet");
            }
            session.setAttribute("petId", petId);
            mav.addObject("pet", pets.get(0));

            mav.addObject("chatGroupFormList", chatGroupFormList);
            mav.addObject("prefectures", petService.findAllPrefectureName());
            mav.addObject("categories", petService.findAllCategory());
            mav.setViewName("/petDetail");
            return mav;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessages", "データが存在しません");
            return new ModelAndView("redirect:/");
        }
    }

    /*ペット登録画面表示*/
    @GetMapping("/addPets")
    public ModelAndView addPets() {
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = (List<String>) session.getAttribute("errorMessages");

        PetForm petForm = new PetForm();
        User user = getLoginUser();

        if (errorMessages != null) {
            mav.addObject("errorMessages", errorMessages);
            session.removeAttribute("errorMessages");
        }

        mav.addObject("user", user);
        mav.addObject("formModel", petForm);
        mav.addObject("prefectures", petService.findAllPrefectureName());
        mav.addObject("categories", petService.findAllCategory());
        mav.setViewName("addPets");
        return mav;
    }

    /*ペット登録機能*/
    @PostMapping("/addPets")
    public ModelAndView addPets(@ModelAttribute("formModel") @Validated({PetForm.PositiveValidGroup1.class, PetForm.PositiveValidGroup2.class, PetForm.PositiveValidGroup3.class, PetForm.PetNameValidGroup1.class, PetForm.PetColorValidGroup1.class, PetForm.ImgValidGroup.class, PetForm.VideoValidGroup.class,})
                                PetForm petForm, BindingResult result, RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<>();

        if (result.hasErrors()) {
            String message = null;
            for (FieldError error : result.getFieldErrors()) {
                message = error.getDefaultMessage();
                errorMessages.add(message);
            }
        }
        //エラーがあった場合は入力した内容を保持して編集画面へ戻す
        if (errorMessages.size() != 0) {
            mav.setViewName("/addPets");
            mav.addObject("errorMessages", errorMessages);
            mav.addObject("formModel", petForm);
            mav.addObject("prefectures", petService.findAllPrefectureName());
            mav.addObject("categories", petService.findAllCategory());
            return mav;
        }

        try {
            petForm.setStatus(1);
            Pet savePet = petService.setPet(petForm);
            petService.savePet(savePet);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessages", "添付ファイルを登録できません");
            mav.addObject("prefectures", petService.findAllPrefectureName());
            mav.addObject("categories", petService.findAllCategory());
            mav.setViewName("redirect:/addPets");
            return mav;
        }
        mav.setViewName("redirect:/");
        return mav;
    }

    /*
     * ペット編集機能
     */
    @PostMapping("/petEdited")
    public ModelAndView petEdit(@RequestParam(name = "id") int id) {
        ModelAndView mav = new ModelAndView();
        PetForm petForm = petService.editPet(id);
        session.setAttribute("petForm", petForm);
        mav.setViewName("redirect:/editPets");

        return mav;
    }

    /*ペット編集画面表示*/
    @SneakyThrows
    @GetMapping("/editPets")
    public ModelAndView editPets() {
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = (List<String>) session.getAttribute("errorMessages");

        PetForm petForm = (PetForm) session.getAttribute("petForm");

        if (errorMessages != null) {
            mav.addObject("errorMessages", errorMessages);
            session.removeAttribute("errorMessages");
        }

        mav.addObject("prefectures", petService.findAllPrefectureName());
        mav.addObject("categories", petService.findAllCategory());
        mav.addObject("formModel", petForm);
        mav.setViewName("editPets");

        return mav;
    }

    /*ペット編集機能*/
    @PostMapping("/editPets")
    public ModelAndView editPets(@ModelAttribute("formModel") @Validated({PetForm.PositiveValidGroup1.class, PetForm.PositiveValidGroup2.class, PetForm.PositiveValidGroup3.class, PetForm.PetNameValidGroup1.class, PetForm.PetColorValidGroup1.class, PetForm.ImgValidGroup.class, PetForm.VideoValidGroup.class,})
                                 PetForm petForm, BindingResult result, RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<>();

        if (result.hasErrors()) {
            String message = null;
            for (FieldError error : result.getFieldErrors()) {
                message = error.getDefaultMessage();
                errorMessages.add(message);
            }
        }
        //エラーがあった場合は入力した内容を保持して編集画面へ戻す
        if (errorMessages.size() != 0) {
            mav.setViewName("/editPets");
            mav.addObject("errorMessages", errorMessages);
            mav.addObject("formModel", petForm);
            mav.addObject("prefectures", petService.findAllPrefectureName());
            mav.addObject("categories", petService.findAllCategory());
            return mav;
        }

        try {
            Pet updatePet = petService.setPet(petForm);
            Pet savePet = petService.updatePet(updatePet);
            petService.savePet(savePet);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessages", "画像を登録できません");
            mav.addObject("prefectures", petService.findAllPrefectureName());
            mav.addObject("categories", petService.findAllCategory());
            mav.setViewName("redirect:/editPets");
            return mav;
        }
        mav.setViewName("editPets");
        return new ModelAndView("redirect:/");
    }
}