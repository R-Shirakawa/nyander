package com.example.nyander.service;

import com.example.nyander.controller.form.UserForm;
import com.example.nyander.repository.PetRepository;
import com.example.nyander.repository.UserRepository;
import com.example.nyander.repository.entity.User;
import com.example.nyander.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static com.example.nyander.security.SecurityConfig.getLoginUser;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SecurityConfig securityConfig;
    @Autowired
    PetRepository petRepository;

    public void save(UserForm form) throws IOException {
        User user = setEntity(form);
        // デフォルトアイコンを生成
        if(user.getIcon() == null) {
            user.setIcon(createDefaultImage(form.getName()));
        }
        userRepository.save(user);
    }

    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public void update(UserForm userForm) throws IOException {
        User user = setEntity(userForm);
        user.setId(getLoginUser().getId());
        if (userForm.getPassword() == null) {
            user.setPassword(getLoginUser().getPassword());
        }
        if (userForm.getImage().isEmpty()) {
            user.setIcon(convertBase64(userForm.getIcon()));
        }
        if (user.getPassword() != null) {
            userRepository.save(user);
        } else {
            // パスワードを更新しないときのメソッド
            user.setUpdatedDate(new Date());
            userRepository.updateWithoutPassword(user.getName(), user.getPostNumber(), user.getAddress1(), user.getAddress2(), user.getAddress3(), user.getIcon(), user.getUpdatedDate(), user.getId());
        }

        securityConfig.setLoginUser(user);
    }

    public UserForm setUserForm(User user) throws IOException {
        UserForm userForm = new UserForm();
        userForm.setId(user.getId());
        userForm.setName(user.getName());
        if (user.getPassword() != null) {
            userForm.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userForm.setPostNumber(user.getPostNumber());
        userForm.setAddress1(user.getAddress1());
        userForm.setAddress2(user.getAddress2());
        userForm.setAddress3(user.getAddress3());
        if (user.getIcon() != null) {
            userForm.setIcon(convertByte(user.getIcon()));
        }
        userForm.setUpdatedDate(new Date());
        return userForm;
    }

    public User setEntity(UserForm userModel) throws IOException {
        User user = new User();
        user.setId(userModel.getId());
        user.setName(userModel.getName());
        System.out.println(userModel.getPassword().length());
        // パスワード
        if (!userModel.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        }
        user.setPostNumber(userModel.getPostNumber());
        user.setAddress1(userModel.getAddress1());
        user.setAddress2(userModel.getAddress2());
        user.setAddress3(userModel.getAddress3());
        // アイコン
        if (userModel.getImage() != null) {
            user.setIcon(convertFile(userModel.getImage()));
        }
        // CreatedDateとUpdateDateはEntityで定義
        return user;
    }

    // アイコンのファイル化(BYTE → Base64)
    public static String convertByte(byte[] results) throws IOException {
        return Base64.getEncoder().encodeToString(results);
    }

    // ファイルのbyte[]化(Base64 → BYTE)
    public byte[] convertFile(MultipartFile file) throws IOException {
        return file.getBytes();
    }

    //base64のbyte[]化
    public byte[] convertBase64(String base64){
        return Base64.getDecoder().decode(base64);
    }

    public byte[] createDefaultImage(String userName) {
        // 画像のサイズ
        int width = 300;
        int height = 300;

        // 画像を生成
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 背景を白で塗りつぶし
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // テキストを描画
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 125));

        // 画像に入れる文字を設定し、描画
        String text = userName.replaceAll("[ 　]", "").substring(0,1);
//        String text = "No Image";
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        int x = (width - textWidth) / 2;
        int y = height / 2;
//        g2d.setColor(Color.CYAN); // 色指定
        g2d.drawString(text, x, y);

        g2d.dispose();
        try {
            // Create a ByteArrayOutputStream to store the image data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            // Get the image data as a byte array
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public UserForm findById(int recruitmentId) throws IOException {
        User user = new User();
        user = userRepository.findById(recruitmentId);
        UserForm userForm = setUserForm(user);
        return userForm;
    }

}
