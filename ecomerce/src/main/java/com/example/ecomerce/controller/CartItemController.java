package com.example.ecomerce.controller;

import com.example.ecomerce.dto.CartItemDto;
import com.example.ecomerce.dto.RegisterDTO;
import com.example.ecomerce.email.EmailSenderService;
import com.example.ecomerce.model.CartItem;
import com.example.ecomerce.model.User;
import com.example.ecomerce.service.CartItemService;
import com.example.ecomerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cartItem")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private EmailSenderService emailSenderService;


//    @PostMapping(value = "/addItems", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> addItems(@RequestBody List<CartItemDto> cartItems) throws IOException {
//        // Convert CartItemDTOs to CartItems
//        List<CartItem> convertedCartItems = convertToCartItems(cartItems);
//
//        // Save the cart items
//        cartItemService.saveProducts(convertedCartItems);
//
//        // Generate HTML content for the email
//        String htmlContent = generateHtmlContent(cartItems);
//
//        // Send the email with HTML content
//        String recipientEmail = "igorpavlov106@gmail.com"; // Replace with the recipient's email address
//        emailSenderService.sendHtmlEmail(recipientEmail, "Your Order Confirmation", htmlContent);
//
//        return new ResponseEntity<>("Cart items added successfully", HttpStatus.OK);
//    }

//    @PostMapping(value = "/addItems", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> addItems(@RequestBody List<CartItemDto> cartItems) throws IOException {
//        // Convert CartItemDTOs to CartItems
//        List<CartItem> convertedCartItems = convertToCartItems(cartItems);
//
//        // Save the cart items
//        cartItemService.saveProducts(convertedCartItems);
//
//        // Generate HTML content for the email
//        String htmlContent = generateHtmlContent(cartItems,"E:\\ecomerce\\E-comerce-store\\E-comerce-store\\ecomerce\\src\\main\\resources\\static\\");
//
//        // Load the image data for each cart item
//        for (CartItemDto cartItem : cartItems) {
//            byte[] itemImageData = Files.readAllBytes(Paths.get("E:\\ecomerce\\E-comerce-store\\E-comerce-store\\ecomerce\\src\\main\\resources\\static\\"+cartItem.getImagePath()));
//            // Assuming cart item image paths are absolute paths
//
//            // Send the email with HTML content and image attachment for each cart item
//            emailSenderService.sendHtmlEmailWithImageAttachment("igorpavlov106@gmail.com", "Your Order Confirmation", htmlContent, itemImageData, "cart_item_" + UUID.randomUUID() + ".png");
//        }
//
//        return new ResponseEntity<>("Cart items added successfully", HttpStatus.OK);
//    }

    @PostMapping(value = "/addItems", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addItems(@RequestBody List<CartItemDto> cartItems) throws IOException {
        // Convert CartItemDTOs to CartItems
        List<CartItem> convertedCartItems = convertToCartItems(cartItems);

        // Save the cart items
        cartItemService.saveProducts(convertedCartItems);

        // Generate HTML content for the email


        // Accumulate attachment data and names
        List<byte[]> attachmentDataList = new ArrayList<>();
        List<String> attachmentNames = new ArrayList<>();
        for (CartItemDto cartItem : cartItems) {
            byte[] itemImageData = Files.readAllBytes(Paths.get("E:\\ecomerce\\E-comerce-store\\E-comerce-store\\ecomerce\\src\\main\\resources\\static\\" + cartItem.getImagePath()));
            attachmentDataList.add(itemImageData);
            attachmentNames.add("cart_item_" + UUID.randomUUID() + ".png");
        }
//        String htmlContent = generateHtmlContent2(cartItems);
        // Send the email with HTML content and all image attachments
        emailSenderService.sendHtmlEmailWithImageAttachments("igorpavlov106@gmail.com", "Your Order Confirmation", cartItems,attachmentDataList, attachmentNames);

        return new ResponseEntity<>("Cart items added successfully", HttpStatus.OK);
    }

    private String generateHtmlContent2(List<CartItemDto> cartItems) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        htmlContentBuilder.append("<html><body>");
        htmlContentBuilder.append("<div style='text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 5px;background-color:bisque;'>");
        htmlContentBuilder.append("<h1>Thank you for shopping at our store!</h1>");

        // Add image for the header
        String imageUrl = "https://www.creativefabrica.com/wp-content/uploads/2018/10/Shopping-cart-logo-by-DEEMKA-STUDIO-580x406.jpg";
        htmlContentBuilder.append("<img src='" + imageUrl + "' alt='Cart Image' style='max-width: 90%; max-height:40%; border-radius:5px;'>");

        htmlContentBuilder.append("<br><br>");
        htmlContentBuilder.append("<p>You ordered:</p>");
        htmlContentBuilder.append("<br><br>");

        // Table header
        htmlContentBuilder.append("<table style='width:100%;'>");
        htmlContentBuilder.append("<tr>");
        htmlContentBuilder.append("<th style='width: 25%; text-align:center;'>Name</th>");
        htmlContentBuilder.append("<th style='width: 25%; text-align:center;'>Description</th>");
        htmlContentBuilder.append("<th style='width: 25%; text-align:center;'>Price</th>");
        htmlContentBuilder.append("<th style='width: 25%; text-align:center;'>Image</th>");
        htmlContentBuilder.append("</tr>");

        String images="jordan-3-red-removebg-preview.png";        // Table rows for cart items
        for (CartItemDto cartItem : cartItems) {
            System.out.println(cartItem.getImagePath());
            htmlContentBuilder.append("<tr>");
            htmlContentBuilder.append("<td>").append(cartItem.getName()).append("</td>");
            htmlContentBuilder.append("<td>").append(cartItem.getDescription()).append("</td>");
            htmlContentBuilder.append("<td>").append(cartItem.getPrice()).append("$</td>");
            htmlContentBuilder.append("<td><img src='").append(images).append("' alt='Product Image' style='max-width: 100px; max-height: 100px;'></td>");
            htmlContentBuilder.append("</tr>");
        }

        // Close table
        htmlContentBuilder.append("</table>");

        htmlContentBuilder.append("</div></body></html>");
        return htmlContentBuilder.toString();
    }


//    private String generateHtmlContent(List<CartItemDto> cartItems) {
//        StringBuilder htmlContentBuilder = new StringBuilder();
//        htmlContentBuilder.append("<html><body>");
//        htmlContentBuilder.append("<div style='text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 5px;background-color:bisque;'>");
//        htmlContentBuilder.append("<h1>Thank you for shopping at out store " +"!</h1>");
//
//        String imageUrl = "https://www.creativefabrica.com/wp-content/uploads/2018/10/Shopping-cart-logo-by-DEEMKA-STUDIO-580x406.jpg";
//        String imgs="jorda-royal-removebg-preview.png";
//        htmlContentBuilder.append("<img src='" + imageUrl + "' alt='Cart Image' style='max-width: 90%; max-height:40%; border-radius:5px;'>");
//        htmlContentBuilder.append("<img src='" + imgs + "' alt='Cart Image' style='max-width: 90%; max-height:40%; border-radius:5px;'>");
//
//        htmlContentBuilder.append("<br><br>");
//        htmlContentBuilder.append("<p>You ordered:</p>");
//        htmlContentBuilder.append("<br><br>");
//        // Column labels
//        // Table header
//        // Table header
//        htmlContentBuilder.append("<table style='width:100%;'>");
//        htmlContentBuilder.append("<tr>");
//        htmlContentBuilder.append("<th>Name</th>");
//        htmlContentBuilder.append("<th>Description</th>");
//        htmlContentBuilder.append("<th>Price</th>");
//        htmlContentBuilder.append("<th>Image</th>");
//        htmlContentBuilder.append("</tr>");
//
//        // Table rows for cart items
//        for (CartItemDto cartItem : cartItems) {
//            htmlContentBuilder.append("<tr>");
//            htmlContentBuilder.append("<td>").append(cartItem.getName()).append("</td>");
//            htmlContentBuilder.append("<td>").append(cartItem.getDescription()).append("</td>");
//            htmlContentBuilder.append("<td>").append(cartItem.getPrice()).append("$</td>");
//
//            System.out.println(cartItem.getImagePath());
//            htmlContentBuilder.append("<td><img src='").append(cartItem.getImagePath()).append("' alt='Product Image' style='max-width: 100px; max-height: 100px;'></td>");
//            htmlContentBuilder.append("</tr>");            htmlContentBuilder.append("</tr>");
//            // Load image from classpath resources
//            ClassPathResource resource = new ClassPathResource(cartItem.getImagePath());
//            try (InputStream inputStream = resource.getInputStream()) {
//                byte[] bytes = StreamUtils.copyToByteArray(inputStream);
//                String base64Image = Base64.getEncoder().encodeToString(bytes);
//                htmlContentBuilder.append("<td><img src='").append("src/main/resources/static/").append(cartItem.getImagePath()).append("' alt='Product Image' style='max-width: 100px; max-height: 100px;'></td>");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            htmlContentBuilder.append("</tr>");
//        }
//
//        // Close table
//        htmlContentBuilder.append("</table>");
//
//        // Total price
//        double totalPrice = cartItems.stream()
//                .mapToDouble(cartItem -> Double.parseDouble(cartItem.getPrice()))
//                .sum();
//        htmlContentBuilder.append("<div style='text-align: right; margin-top: 20px; margin-right:140px;'>");
//        htmlContentBuilder.append("<p>Grand Total: ").append(totalPrice).append("$</p>");
//        htmlContentBuilder.append("</div>");
//
//        htmlContentBuilder.append("</div></body></html>");
//        return htmlContentBuilder.toString();
//    }
//
//
//
//
//
//
//    private List<CartItem> convertToCartItems(List<CartItemDto> cartItemDTOs) {
//        List<CartItem> cartItems = new ArrayList<>();
//        for (CartItemDto dto : cartItemDTOs) {
//            CartItem item = new CartItem();
//            item.setName(dto.getName());
//            item.setDescription(dto.getDescription());
//            item.setPrice(dto.getPrice());
//            item.setImagePath(dto.getImagePath());
//            // Decode base64 image
////            String base64Image = dto.getImagePath();
////            byte[] decodedImage = Base64.getDecoder().decode(base64Image);
////
////            // Call a separate method to set the image data in CartItem
////            setImageBytes(item, decodedImage);
//
//            cartItems.add(item);
//        }
//        return cartItems;
//    }

    private String generateHtmlContent(List<CartItemDto> cartItems, List<byte[]> attachmentDataList) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        htmlContentBuilder.append("<html><body>");
        htmlContentBuilder.append("<div style='text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 5px;background-color:bisque;'>");
        htmlContentBuilder.append("<h1>Thank you for shopping at our store!</h1>");

        // Add image for the header
        String imageUrl = "https://www.creativefabrica.com/wp-content/uploads/2018/10/Shopping-cart-logo-by-DEEMKA-STUDIO-580x406.jpg";
        htmlContentBuilder.append("<img src='" + imageUrl + "' alt='Cart Image' style='max-width: 90%; max-height:40%; border-radius:5px;'>");

        htmlContentBuilder.append("<br><br>");
        htmlContentBuilder.append("<p>You ordered:</p>");
        htmlContentBuilder.append("<br><br>");

        // Table header
        htmlContentBuilder.append("<table style='width:100%;'>");
        htmlContentBuilder.append("<tr>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Name</th>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Description</th>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Price</th>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Image</th>");
        htmlContentBuilder.append("</tr>");
//        htmlContentBuilder.append("<th>Image</th>");
        htmlContentBuilder.append("</tr>");

        // Table rows for cart items
        for (int i = 0; i < cartItems.size(); i++) {
            CartItemDto cartItem = cartItems.get(i);
            byte[] imageData = attachmentDataList.get(i);
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            System.out.println(cartItem);
            htmlContentBuilder.append("<tr>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'>").append(cartItem.getName()).append("</td>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'>").append(cartItem.getDescription()).append("</td>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'>").append(cartItem.getPrice()).append("$</td>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'><img src='").append(cartItem.getImagePath()).append("' alt='Product Image' style='max-width: 100px; max-height: 100px;'></td>");
//            htmlContentBuilder.append("</tr>");            htmlContentBuilder.append("</tr>");
            htmlContentBuilder.append("</tr>");
        }

        // Close table
        htmlContentBuilder.append("</table>");

        double totalPrice = cartItems.stream()
                .mapToDouble(cartItem -> Double.parseDouble(cartItem.getPrice()))
                .sum();
        htmlContentBuilder.append("<div style='text-align: right; margin-top: 20px; margin-right:140px;'>");
        htmlContentBuilder.append("<p>Grand Total: ").append(totalPrice).append("$</p>");
        htmlContentBuilder.append("</div>");

        htmlContentBuilder.append("</div></body></html>");
        return htmlContentBuilder.toString();
    }





    private List<CartItem> convertToCartItems(List<CartItemDto> cartItemDTOs) {
        List<CartItem> cartItems = new ArrayList<>();
        for (CartItemDto dto : cartItemDTOs) {
            CartItem item = new CartItem();
            item.setName(dto.getName());
            item.setDescription(dto.getDescription());
            item.setPrice(dto.getPrice());
            item.setImagePath(dto.getImagePath());
            cartItems.add(item);
        }
        return cartItems;
    }
    private void setImageBytes(CartItem item, byte[] imageData) {
        // Here, you can set the image data in whatever way is appropriate for your CartItem model
        // For example, if you have a method like setImageBytes(byte[] imageData), you can call it here
        // Otherwise, you can store the image data in a different field or handle it according to your model's requirements
        // item.setImageBytes(imageData);
        item.setImagePath(String.valueOf(imageData));
    }





}
