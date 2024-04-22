//package com.example.doanltttbdd.Email;
//
//import android.util.Log;
//
//import com.example.doanltttbdd.Database.CreateDatabase;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Properties;
//import java.util.Random;
//
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//public class EmailSender {
//    private static final String TAG = "EmailSender"; // Tag cho Logcat
//    private static final String TAG2 = "Database"; // Tag cho Logcat
//    private String senderEmail = "nogen913@gmail.com";
//    private String reciverEmail = "";
//    private String passwordSender = "uhkh wchk grye dolb";
//    private String host = "smtp.gmail.com";
//    private CreateDatabase database;
//
//    public EmailSender(String reciverEmail, CreateDatabase database) {
//        this.reciverEmail = reciverEmail;
//        this.database = database;
//    }
//
//    // Hàm này tạo mã OTP ngẫu nhiên gồm 4 chữ số
//    private String generateOTP() {
//        Random random = new Random();
//        StringBuilder otp = new StringBuilder();
//        for (int i = 0; i < 4; i++) {
//            otp.append(random.nextInt(10)); // Tạo số ngẫu nhiên từ 0 đến 9
//        }
//        return otp.toString();
//    }
//
//    private String hashOTP(String otp) {
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] encodedHash = digest.digest(otp.getBytes());
//
//            // Chuyển đổi mảng byte sang dạng hex
//            StringBuilder hexString = new StringBuilder();
//            for (byte b : encodedHash) {
//                String hex = Integer.toHexString(0xff & b);
//                if (hex.length() == 1) hexString.append('0');
//                hexString.append(hex);
//            }
//            return hexString.toString();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private String getCurrentTimestamp() {
//        // Trả về timestamp hiện tại
//        return String.valueOf(System.currentTimeMillis());
//    }
//
//    public void sendEmail() {
//        try {
//            Properties properties = System.getProperties();
//            properties.put("mail.smtp.host", host);
//            properties.put("mail.smtp.port", "465");
//            properties.put("mail.smtp.ssl.enable", "true");
//            properties.put("mail.smtp.auth", "true");
//
//            Session session = Session.getInstance(properties, new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(senderEmail, passwordSender);
//                }
//            });
//
//            MimeMessage mimeMessage = new MimeMessage(session);
//            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(reciverEmail));
//            mimeMessage.setSubject("Mã OTP của bạn");
//            String otp = generateOTP();
//            String hashOTP = hashOTP(otp);
//            mimeMessage.setText("Mã OTP của bạn: " + otp);
//            long result = database.addOtp(reciverEmail, hashOTP, getCurrentTimestamp());
//            if (result != -1) {
//                Log.d(TAG2, "OTP inserted successfully into database.");
//            } else {
//                Log.e(TAG2, "Failed to insert OTP into database.");
//            }
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Transport.send(mimeMessage);
//                        Log.d(TAG, "Email sent successfully."); // In thông điệp thành công ra Logcat
//                    } catch (MessagingException e) {
//                        Log.e(TAG, "Failed to send email: " + e.getMessage()); // In thông điệp lỗi ra Logcat
//                        throw new RuntimeException(e);
//                    }
//                }
//            });
//            thread.start();
//        } catch (AddressException e) {
//            Log.e(TAG, "Failed to send email: " + e.getMessage()); // In thông điệp lỗi ra Logcat
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            Log.e(TAG, "Failed to send email: " + e.getMessage()); // In thông điệp lỗi ra Logcat
//            e.printStackTrace();
//        }
//    }
//}
