package kg.neobis.rentit.service;

import kg.neobis.rentit.entity.ImageUser;
import kg.neobis.rentit.repository.ImageUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageUserService {

    private final ImageUserRepository imageUserRepository;

    public ImageUser saveImageUser(ImageUser imageUser) {
        return imageUserRepository.save(imageUser);
    }
}
