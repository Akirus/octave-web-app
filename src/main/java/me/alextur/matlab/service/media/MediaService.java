package me.alextur.matlab.service.media;

import me.alextur.matlab.model.media.Media;
import me.alextur.matlab.repository.media.MediaRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

/**
 * @author Alex Turchynovich
 */
@Component
public class MediaService {

    private MediaRepository mediaRepository;

    public MediaService(MediaRepository pMediaRepository) {
        mediaRepository = pMediaRepository;
    }

    public Media createMedia(CreateMediaRequest pCreateMediaRequest){
        Media media = new Media();
        media.setName(pCreateMediaRequest.getName());
        media.setContentType(pCreateMediaRequest.getContentType());

        // TODO add content validation.
        byte[] content = Base64.decodeBase64(pCreateMediaRequest.getContent());
        media.setContent(content);

        media = mediaRepository.saveAndFlush(media);

        return media;
    }
}
