package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.UserBean;
import com.floriantoenjes.ee.forum.ejb.model.User;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Named
@RequestScoped
public class ProfileController {
    private final static int MAX_IMAGE_LENGTH = 100;

    private Part part;

    @Inject
    private SignInController signInController;

    @EJB
    private UserBean userBean;

    public void saveProfile() {
        User user = signInController.getUser();

        try {
            if (part != null) {
                InputStream in = part.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                for (int length = 0; (length = in.read(buffer)) > 0;) {
                    baos.write(buffer, 0, length);
                }
                user.setAvatar(scale(baos.toByteArray()));
            }
            userBean.merge(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    private byte[] scale(byte[] avatar) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(avatar);
        BufferedImage originalBufferedImage = ImageIO.read(byteArrayInputStream);

        double originalWidth = (double) originalBufferedImage.getWidth();
        double originalHeight = (double) originalBufferedImage.getHeight();

        double relevantLength = originalWidth > originalHeight ? originalWidth : originalHeight;

        double scaleFactor = MAX_IMAGE_LENGTH / relevantLength;

        int width = (int) Math.round(originalWidth * scaleFactor);
        int height = (int) Math.round(originalHeight * scaleFactor);

        BufferedImage resizedBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = resizedBufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        AffineTransform affineTransform = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
        g2d.drawRenderedImage(originalBufferedImage, affineTransform);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedBufferedImage, "PNG", baos);

        return baos.toByteArray();
    }
}
