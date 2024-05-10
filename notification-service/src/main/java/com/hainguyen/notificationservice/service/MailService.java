package com.hainguyen.notificationservice.service;

import com.hainguyen.notificationservice.events.upstream.UserContactResponse;
import com.hainguyen.notificationservice.model.Mail;
import com.hainguyen.notificationservice.model.Notification;
import com.hainguyen.notificationservice.events.upstream.OrderShippingStatusChanged;
import com.hainguyen.notificationservice.model.OrderStatus;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;


    public Mono<Void> sendMail(Mail mail) {
        return Mono.just(mail)
                .flatMap(m -> {
                    try {
                        // Create a MimeMessage
                        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

                        // Use the MimeMessageHelper to add the details to the message
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                        helper.setFrom("E-commerce_Store");
                        helper.setTo(mail.getRecipient());
                        helper.setSubject(mail.getSubject());
                        helper.setText(convertToHtml(mail.getContent()), true); // Set the second parameter to 'true' to enable HTML

                        // Send the mail
                        log.info("Sending mail to {}", mail.getRecipient());
                        javaMailSender.send(mimeMessage);
                        log.info("Mail Sent Successfully...");
                        return Mono.empty();
                    } catch (Exception e) {
                        log.error("Error while Sending Mail", e);
                        return Mono.empty();
                    }
                });


    }




    public String sendSimpleMail() {
        try {
            // Create a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Use the MimeMessageHelper to add the details to the message
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("sender");
            helper.setTo("forexams25@gmail.com");
            helper.setSubject("Your order has been on the way!");
            helper.setText(convertToHtml("a"), true); // Set the second parameter to 'true' to enable HTML

            // Send the mail
            javaMailSender.send(mimeMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            log.error("Error while Sending Mail", e);
            return "Error while Sending Mail";
        }
    }


    public static String constructMailContent(OrderStatus orderStatus) {
        return switch (orderStatus) {
            case ON_HOLD_OUT_OF_STOCK -> "Your order is on hold because some items are out of stock, please wait for a while or contact us for more information";
            case ON_HOLD_SHIPPING -> "Your order is on hold because the shipping address is incorrect, please update your shipping address so that we can process your order";
            case ON_HOLD_PAYMENT_FAILED -> "Your order is on hold because the payment failed, please update your payment method so that we can process your order";
            case DELIVERED -> "Your order has been delivered, thank you for shopping with us!";
            case SHIPPED -> "Your order has been shipped, you can track your order with the tracking number provided in the email";
            case RETURNED -> "Your order has been returned, you will receive a refund within 3-5 business days. Please contact us for more information";
            default -> "Message from the store, your order status has been updated, please check your order status in your account";
        };
    }


    public static String constructMailSubject(OrderStatus orderStatus) {
        String subject = switch (orderStatus) {
            case ON_HOLD_OUT_OF_STOCK, ON_HOLD_SHIPPING, ON_HOLD_PAYMENT_FAILED -> "Your order is on hold";
            case DELIVERED -> "Your order has been delivered";
            case SHIPPED -> "Your order has been on the way!";
            case RETURNED -> "Your order has been returned";
            default -> "Message from the store";
        };

        return subject;
    }

    private String convertToHtml(String message) {
        return "<p>" + message + "</p>";
    }


//    private String convertToHtml(String message) {
//        return """
//                <!doctype html>
//                <html lang="en">
//                  <head>
//                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
//                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
//                    <title>Simple Transactional Email</title>
//                    <style media="all" type="text/css">
//                    /* -------------------------------------
//                    GLOBAL RESETS
//                ------------------------------------- */
//                   \s
//                    body {
//                      font-family: Helvetica, sans-serif;
//                      -webkit-font-smoothing: antialiased;
//                      font-size: 16px;
//                      line-height: 1.3;
//                      -ms-text-size-adjust: 100%;
//                      -webkit-text-size-adjust: 100%;
//                    }
//                   \s
//                    table {
//                      border-collapse: separate;
//                      mso-table-lspace: 0pt;
//                      mso-table-rspace: 0pt;
//                      width: 100%;
//                    }
//                   \s
//                    table td {
//                      font-family: Helvetica, sans-serif;
//                      font-size: 16px;
//                      vertical-align: top;
//                    }
//                    /* -------------------------------------
//                    BODY & CONTAINER
//                ------------------------------------- */
//                   \s
//                    body {
//                      background-color: #f4f5f6;
//                      margin: 0;
//                      padding: 0;
//                    }
//                   \s
//                    .body {
//                      background-color: #f4f5f6;
//                      width: 100%;
//                    }
//                   \s
//                    .container {
//                      margin: 0 auto !important;
//                      max-width: 600px;
//                      padding: 0;
//                      padding-top: 24px;
//                      width: 600px;
//                    }
//                   \s
//                    .content {
//                      box-sizing: border-box;
//                      display: block;
//                      margin: 0 auto;
//                      max-width: 600px;
//                      padding: 0;
//                    }
//                    /* -------------------------------------
//                    HEADER, FOOTER, MAIN
//                ------------------------------------- */
//                   \s
//                    .main {
//                      background: #ffffff;
//                      border: 1px solid #eaebed;
//                      border-radius: 16px;
//                      width: 100%;
//                    }
//                   \s
//                    .wrapper {
//                      box-sizing: border-box;
//                      padding: 24px;
//                    }
//                   \s
//                    .footer {
//                      clear: both;
//                      padding-top: 24px;
//                      text-align: center;
//                      width: 100%;
//                    }
//                   \s
//                    .footer td,
//                    .footer p,
//                    .footer span,
//                    .footer a {
//                      color: #9a9ea6;
//                      font-size: 16px;
//                      text-align: center;
//                    }
//                    /* -------------------------------------
//                    TYPOGRAPHY
//                ------------------------------------- */
//                   \s
//                    p {
//                      font-family: Helvetica, sans-serif;
//                      font-size: 16px;
//                      font-weight: normal;
//                      margin: 0;
//                      margin-bottom: 16px;
//                    }
//                   \s
//                    a {
//                      color: #0867ec;
//                      text-decoration: underline;
//                    }
//                    /* -------------------------------------
//                    BUTTONS
//                ------------------------------------- */
//                   \s
//                    .btn {
//                      box-sizing: border-box;
//                      min-width: 100% !important;
//                      width: 100%;
//                    }
//                   \s
//                    .btn > tbody > tr > td {
//                      padding-bottom: 16px;
//                    }
//                   \s
//                    .btn table {
//                      width: auto;
//                    }
//                   \s
//                    .btn table td {
//                      background-color: #ffffff;
//                      border-radius: 4px;
//                      text-align: center;
//                    }
//                   \s
//                    .btn a {
//                      background-color: #ffffff;
//                      border: solid 2px #0867ec;
//                      border-radius: 4px;
//                      box-sizing: border-box;
//                      color: #0867ec;
//                      cursor: pointer;
//                      display: inline-block;
//                      font-size: 16px;
//                      font-weight: bold;
//                      margin: 0;
//                      padding: 12px 24px;
//                      text-decoration: none;
//                      text-transform: capitalize;
//                    }
//                   \s
//                    .btn-primary table td {
//                      background-color: #0867ec;
//                    }
//                   \s
//                    .btn-primary a {
//                      background-color: #0867ec;
//                      border-color: #0867ec;
//                      color: #ffffff;
//                    }
//                   \s
//                    @media all {
//                      .btn-primary table td:hover {
//                        background-color: #ec0867 !important;
//                      }
//                      .btn-primary a:hover {
//                        background-color: #ec0867 !important;
//                        border-color: #ec0867 !important;
//                      }
//                    }
//                   \s
//                    /* -------------------------------------
//                    OTHER STYLES THAT MIGHT BE USEFUL
//                ------------------------------------- */
//                   \s
//                    .last {
//                      margin-bottom: 0;
//                    }
//                   \s
//                    .first {
//                      margin-top: 0;
//                    }
//                   \s
//                    .align-center {
//                      text-align: center;
//                    }
//                   \s
//                    .align-right {
//                      text-align: right;
//                    }
//                   \s
//                    .align-left {
//                      text-align: left;
//                    }
//                   \s
//                    .text-link {
//                      color: #0867ec !important;
//                      text-decoration: underline !important;
//                    }
//                   \s
//                    .clear {
//                      clear: both;
//                    }
//                   \s
//                    .mt0 {
//                      margin-top: 0;
//                    }
//                   \s
//                    .mb0 {
//                      margin-bottom: 0;
//                    }
//                   \s
//                    .preheader {
//                      color: transparent;
//                      display: none;
//                      height: 0;
//                      max-height: 0;
//                      max-width: 0;
//                      opacity: 0;
//                      overflow: hidden;
//                      mso-hide: all;
//                      visibility: hidden;
//                      width: 0;
//                    }
//                   \s
//                    .powered-by a {
//                      text-decoration: none;
//                    }
//                   \s
//                    /* -------------------------------------
//                    RESPONSIVE AND MOBILE FRIENDLY STYLES
//                ------------------------------------- */
//                   \s
//                    @media only screen and (max-width: 640px) {
//                      .main p,
//                      .main td,
//                      .main span {
//                        font-size: 16px !important;
//                      }
//                      .wrapper {
//                        padding: 8px !important;
//                      }
//                      .content {
//                        padding: 0 !important;
//                      }
//                      .container {
//                        padding: 0 !important;
//                        padding-top: 8px !important;
//                        width: 100% !important;
//                      }
//                      .main {
//                        border-left-width: 0 !important;
//                        border-radius: 0 !important;
//                        border-right-width: 0 !important;
//                      }
//                      .btn table {
//                        max-width: 100% !important;
//                        width: 100% !important;
//                      }
//                      .btn a {
//                        font-size: 16px !important;
//                        max-width: 100% !important;
//                        width: 100% !important;
//                      }
//                    }
//                    /* -------------------------------------
//                    PRESERVE THESE STYLES IN THE HEAD
//                ------------------------------------- */
//                   \s
//                    @media all {
//                      .ExternalClass {
//                        width: 100%;
//                      }
//                      .ExternalClass,
//                      .ExternalClass p,
//                      .ExternalClass span,
//                      .ExternalClass font,
//                      .ExternalClass td,
//                      .ExternalClass div {
//                        line-height: 100%;
//                      }
//                      .apple-link a {
//                        color: inherit !important;
//                        font-family: inherit !important;
//                        font-size: inherit !important;
//                        font-weight: inherit !important;
//                        line-height: inherit !important;
//                        text-decoration: none !important;
//                      }
//                      #MessageViewBody a {
//                        color: inherit;
//                        text-decoration: none;
//                        font-size: inherit;
//                        font-family: inherit;
//                        font-weight: inherit;
//                        line-height: inherit;
//                      }
//                    }
//                    </style>
//                  </head>
//                  <body>
//                    <table role="presentation" border="0" cellpadding="0" cellspacing="0" class="body">
//                      <tr>
//                        <td>&nbsp;</td>
//                        <td class="container">
//                          <div class="content">
//
//                            <!-- START CENTERED WHITE CONTAINER -->
//                            <span class="preheader">This is preheader text. Some clients will show this text as a preview.</span>
//                            <table role="presentation" border="0" cellpadding="0" cellspacing="0" class="main">
//
//                              <!-- START MAIN CONTENT AREA -->
//                              <tr>
//                                <td class="wrapper">
//                                  <p>Hi there</p>
//                                  <p>Sometimes you just want to send a simple HTML email with a simple design and clear call to action. This is it.</p>
//                                  <table role="presentation" border="0" cellpadding="0" cellspacing="0" class="btn btn-primary">
//                                    <tbody>
//                                      <tr>
//                                        <td align="left">
//                                          <table role="presentation" border="0" cellpadding="0" cellspacing="0">
//                                            <tbody>
//                                              <tr>
//                                                <td> <a href="http://htmlemail.io" target="_blank">Call To Action</a> </td>
//                                              </tr>
//                                            </tbody>
//                                          </table>
//                                        </td>
//                                      </tr>
//                                    </tbody>
//                                  </table>
//                                  <p>This is a really simple email template. It's sole purpose is to get the recipient to click the button with no distractions.</p>
//                                  <p>Good luck! Hope it works.</p>
//                                </td>
//                              </tr>
//
//                              <!-- END MAIN CONTENT AREA -->
//                              </table>
//
//                            <!-- START FOOTER -->
//                            <div class="footer">
//                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
//                                <tr>
//                                  <td class="content-block">
//                                    <span class="apple-link">Company Inc, 7-11 Commercial Ct, Belfast BT1 2NB</span>
//                                    <br> Don't like these emails? <a href="http://htmlemail.io/blog">Unsubscribe</a>.
//                                  </td>
//                                </tr>
//                                <tr>
//                                  <td class="content-block powered-by">
//                                    Powered by <a href="http://htmlemail.io">HTMLemail.io</a>
//                                  </td>
//                                </tr>
//                              </table>
//                            </div>
//
//                            <!-- END FOOTER -->
//                           \s
//                <!-- END CENTERED WHITE CONTAINER --></div>
//                        </td>
//                        <td>&nbsp;</td>
//                      </tr>
//                    </table>
//                  </body>
//                </html>
//                """;
//    }

}
