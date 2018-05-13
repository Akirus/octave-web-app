package me.alextur.matlab.servlet;

import me.alextur.matlab.model.media.Media;
import me.alextur.matlab.repository.media.MediaRepository;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Alex Turchynovich
 */
public class MediaServlet extends HttpServlet {

    private MediaRepository mediaRepository;

    public MediaServlet(MediaRepository pMediaRepository){
        this.mediaRepository = pMediaRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = getId(req);

        Media media = null;

        if (id != null) {
            media = mediaRepository.findOne(id);
        }

        if (media != null){
            resp.setStatus(200);
            resp.setContentType(media.getContentType());
            byte[] content = media.getContent();
            resp.setContentLength(content.length);
            OutputStream outputstream = resp.getOutputStream();
            outputstream.write(content);
        } else {
            resp.setStatus(404);
        }
    }

    private Long getId(HttpServletRequest req) {
        String path = new UrlPathHelper().getPathWithinApplication(req);

        String mediaId = path.substring(path.lastIndexOf('/') + 1);

        try {
            return Long.parseLong(mediaId);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
