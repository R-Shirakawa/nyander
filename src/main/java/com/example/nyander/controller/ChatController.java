package com.example.nyander.controller;

import com.example.nyander.controller.form.ChatForm;
import com.example.nyander.controller.form.ChatGroupForm;
import com.example.nyander.controller.form.PetForm;
import com.example.nyander.controller.form.UserForm;
import com.example.nyander.repository.entity.ChatGroup;
import com.example.nyander.repository.entity.Pet;
import com.example.nyander.repository.entity.User;
import com.example.nyander.service.ChatService;
import com.example.nyander.service.PetService;
import com.example.nyander.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.nyander.security.SecurityConfig.getLoginUser;

@Controller
public class ChatController {
    @Autowired
    ChatService chatService;

    @Autowired
    HttpSession session;

    @Autowired
    PetService petService;

    @Autowired
    UserService userService;

    /*
     * 同意書画面表示
     */
    @GetMapping("/entry")
    public ModelAndView newEntry(@RequestParam(name = "petId", required = false) String petId) {
        ModelAndView mav = new ModelAndView();

        List<PetForm> pets = petService.findById(Integer.parseInt(petId));

        mav.addObject("pet", pets.get(0));
        mav.setViewName("/entry");
        return mav;
    }

    /*
     * チャット画面表示
     */
    @GetMapping("/new_chat")
    public ModelAndView newChat() {
        ModelAndView mav = new ModelAndView();

        User loginUser = getLoginUser();

        String petIdString = (String) session.getAttribute("petId");
        int petId = Integer.parseInt(petIdString);
        // ペットの情報を取得
        List<PetForm> petFormList = petService.findById(petId);
        // ペット情報から recruitmentId を取得
        int recruitmentId = petFormList.get(0).getRecruitmentId();
        // チャットグループを作成
        List<ChatForm> chatForms = new ArrayList<>();
        ChatGroup chatGroup = chatService.saveChatGroup(petId, recruitmentId, getLoginUser().getId());

        // 作成したチャットグループの ID を取得
        int chatGroupId = chatGroup.getId();

        mav.addObject("loginUser", loginUser);
        mav.addObject("pet", petFormList.get(0));
        mav.addObject("chatGroupId", chatGroupId);
        mav.addObject("chatForms", chatForms);
        mav.setViewName("/chatPage");
        return mav;
//        PetForm petForm = (PetForm) session.getAttribute("petList");
//        int chatGroupId = (int) session.getAttribute("chatGroupId");
//
//        // チャット画面にリダイレクト
//        mav.addObject("pet", petForm);
//        mav.setViewName("redirect:/chat_group?chatGroupId=" + chatGroupId);
//        return mav;
    }

    /*
     * チャットグループ作成
     */
    @PostMapping("/new_chat")
    public ModelAndView saveChat(){
        ModelAndView mav = new ModelAndView();

        User loginUser = getLoginUser();

        String petIdString = (String) session.getAttribute("petId");
        int petId = Integer.parseInt(petIdString);
        // ペットの情報を取得
        List<PetForm> petFormList = petService.findById(petId);
        // ペット情報から recruitmentId を取得
        int recruitmentId = petFormList.get(0).getRecruitmentId();
        // チャットグループを作成
        List<ChatForm> chatForms = new ArrayList<>();
        ChatGroup chatGroup = chatService.saveChatGroup(petId, recruitmentId, getLoginUser().getId());



        // 作成したチャットグループの ID を取得
        int chatGroupId = chatGroup.getId();

        mav.addObject("loginUser", loginUser);
        mav.addObject("pet", petFormList.get(0));
        mav.addObject("chatGroupId", chatGroupId);
        mav.addObject("chatForms", chatForms);
        mav.setViewName("/chatPage");
        return mav;
    }

    /*
     * 2回目以降の画面表示の機能
     */
    @PostMapping("/chat_group")
    public ModelAndView chatGroup(@RequestParam(name = "petId")int petId, @RequestParam(name = "id")int id) throws IOException {
        ModelAndView mav = new ModelAndView();

        User loginUser = getLoginUser();
        List<ChatForm> chatForms = chatService.findAllChats(id);
        List<PetForm> petFormList = petService.findById(petId);

        PetForm petForm = petFormList.get(0);

        //募集者の名前を取得
        String recruimentName = userService.findById(petForm.getRecruitmentId()).getName();

        mav.addObject("recruimentName", recruimentName);
        mav.addObject("loginUser", loginUser);
        mav.addObject("pet", petForm);
        mav.addObject("chatGroupId", id);
        mav.addObject("chatForms", chatForms);
        mav.setViewName("/chatPage");
        return mav;
    }

    @GetMapping("/chatPage")
    public ModelAndView newChatGroup(@RequestParam(name = "chatGroupId") int id) throws IOException {
        ModelAndView mav = new ModelAndView();
        User loginUser = getLoginUser();
        List<ChatForm> chatForms = chatService.findAllChats(id);
        // ペットの情報を取得
        String petIdString = (String) session.getAttribute("petId");
        int petId = chatService.findPetId(id);
        List<PetForm> petFormList = petService.findById(petId);

        PetForm petForm = petFormList.get(0);
        //募集者の名前を取得
        String recruimentName = userService.findById(petForm.getRecruitmentId()).getName();

        mav.addObject("recruimentName", recruimentName);
        mav.addObject("loginUser", loginUser);
        mav.addObject("pet", petForm);
        mav.addObject("chatGroupId", id);
        mav.addObject("chatForms", chatForms);
        mav.setViewName("/chatPage");
        return mav;
    }

    @PostMapping("/add_chat")
    public ModelAndView addChat(@RequestParam(name = "chatGroupId") int chatGroupId,
                                @RequestParam(name = "chatText") String chatText,
                                RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        chatService.saveChatText(getLoginUser().getId(), chatGroupId, chatText);
        redirectAttributes.addFlashAttribute("errorMessages", "・不正なパラメータが入力されました");
        return new ModelAndView("redirect:/chatPage?chatGroupId=" + chatGroupId);
    }
    @GetMapping("/chatGroupUpdatedCheck")
    @ResponseBody
    public  String chatGroupUpdatedCheck(@RequestParam(name = "chatId", required = false)Integer chatId,
                                         @RequestParam(name = "chatGroupId")int chatGroupId){
        String result;
        int chatId2 =chatService.findFirstChat(chatGroupId);
        if (chatId == null ) {
            if (chatId2 == 0) {
                result = "true";
                return result;
            } else {
                result = "false";
                return result;
            }

        }
        if (chatId == chatId2){
            result = "true";
        } else {
            result = "false";
        }
        return result;
    }
    @GetMapping("/re_chat_group")
    @ResponseBody
    public ModelAndView reChatGroup(@RequestParam(name = "chatGroupId") int id,
                                    @RequestParam(name = "petId") int petId) throws IOException {
        ModelAndView mav = new ModelAndView();

        User loginUser = getLoginUser();
        List<ChatForm> chatForms = chatService.findAllChats(id);
        List<PetForm> petFormList = petService.findById(petId);

        PetForm petForm = petFormList.get(0);

        //募集者の名前を取得
        String recruimentName = userService.findById(petForm.getRecruitmentId()).getName();

        mav.addObject("recruimentName", recruimentName);
        mav.addObject("loginUser", loginUser);
        mav.addObject("pet", petForm);
        mav.addObject("chatGroupId", id);
        mav.addObject("chatForms", chatForms);
        mav.setViewName("/chat_group");
        return mav;
//        ModelAndView mav = new ModelAndView();
//        User loginUser = getLoginUser();
//        List<ChatForm> chatForms = chatService.findAllChats(id);
//        PetForm petForm = new PetForm();
//        petForm.setName(petName);
//
//        String recruimentName = userService.findById(petForm.getRecruitmentId()).getName();
//
//        mav.addObject("recruimentName", recruimentName);
//        mav.addObject("loginUser", loginUser);
//        mav.addObject("pet", petForm);
//        mav.addObject("chatGroupId", id);
//        mav.addObject("chatForms", chatForms);
//        mav.setViewName("/chat_group");
//        return mav;
    }

    /*
     * チャット一覧画面表示
     */
    @GetMapping("/chatList")
    public ModelAndView chatList() throws IOException {
        ModelAndView mav = new ModelAndView();

        //チャットグループ情報を取得(pet情報を含む)
        List<ChatGroupForm> chatGroupFormList = chatService.findAllChatGroup();
        //自身のチャット情報を取得(user情報を含む)
        List<ChatForm> chats = chatService.findByUserId(getLoginUser().getId());

        User loginUser = getLoginUser();

        List<ChatGroupForm> filteredChatGroups = chatGroupFormList.stream()
                .filter(chatGroupForm -> chatGroupForm.getRecruitmentId() == loginUser.getId() || chatGroupForm.getApplyId() == loginUser.getId())
                .collect(Collectors.toList());

//        mav.addObject("chatGroupId", chatGroupId);
        mav.addObject("chatGroupFormList", filteredChatGroups);
        mav.addObject("chats", chats);
        mav.addObject("loginUser",loginUser);
        mav.setViewName("/chatList");
        return mav;
    }
}
