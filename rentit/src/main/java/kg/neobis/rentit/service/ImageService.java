package kg.neobis.rentit.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.neobis.rentit.entity.Image;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final String urlKey = "cloudinary://397182221744884:w661eSu_GFp7rzzZ-qNNogxcHM4@dwtftrmk6";

    private final Cloudinary cloudinary = new Cloudinary((urlKey));

    @SneakyThrows
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @SneakyThrows
    public Image saveImage(MultipartFile file) {
        if (!file.isEmpty()) {
            File saveFile = Files.createTempFile(
                            System.currentTimeMillis() + "",
                            Objects.requireNonNull
                                            (file.getOriginalFilename(), "File must have an extension")
                                    .substring(file.getOriginalFilename().lastIndexOf("."))
                    )
                    .toFile();

            file.transferTo(saveFile);

            Map upload = cloudinary.uploader().upload(saveFile, ObjectUtils.emptyMap());

            Image image = new Image();

            image.setUrl((String) upload.get("url"));
            image.setPublicId((String) upload.get("public_id"));

            return imageRepository.save(image);
        }

        return null;
    }

    @SneakyThrows
    public Image replaceImage(Long id, MultipartFile file) {
        if (!file.isEmpty()) {
            File saveFile = Files.createTempFile(
                            System.currentTimeMillis() + "",
                            Objects.requireNonNull
                                            (file.getOriginalFilename(), "File must have an extension")
                                    .substring(file.getOriginalFilename().lastIndexOf("."))
                    )
                    .toFile();

            file.transferTo(saveFile);

            Map upload = cloudinary.uploader().upload(saveFile, ObjectUtils.emptyMap());

            Image image =  imageRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Could not find image by id = " + id));

            if(!image.getPublicId().equals(" ")) {
                cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
            }

            image.setUrl((String) upload.get("url"));
            image.setPublicId((String) upload.get("public_id"));

            return imageRepository.save(image);
        }

        return null;
    }

    @SneakyThrows
    public Image replaceProductImage(MultipartFile file, Image image) {
        File saveFile = Files.createTempFile(
                        System.currentTimeMillis() + "",
                        Objects.requireNonNull
                                        (file.getOriginalFilename(), "File must have an extension")
                                .substring(file.getOriginalFilename().lastIndexOf("."))
                )
                .toFile();

        file.transferTo(saveFile);

        Map upload = cloudinary.uploader().upload(saveFile, ObjectUtils.emptyMap());

        cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());

        image.setUrl((String) upload.get("url"));
        image.setPublicId((String) upload.get("public_id"));

        return imageRepository.save(image);
    }

}
