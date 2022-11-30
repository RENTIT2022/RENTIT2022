package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.BlockedUserDto;
import kg.neobis.rentit.dto.ComplaintDto;
import kg.neobis.rentit.dto.ComplaintRegistrationDto;
import kg.neobis.rentit.entity.Complaint;
import kg.neobis.rentit.entity.User;
import kg.neobis.rentit.exception.BadRequestException;
import kg.neobis.rentit.exception.ProductViolationException;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.repository.ComplaintRepository;
import kg.neobis.rentit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    private final UserRepository userRepository;


    public String addComplaint(ComplaintRegistrationDto dto) {
        User addressee = userRepository.findById(dto.getUserId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User was not found with ID: " + dto.getUserId())
                );

        User sender = getAuthentication();

        if (sender.getId().equals(addressee.getId())) {
            throw new BadRequestException("Вы не можете пожаловаться на свой профиль, лол.");
        }

        Complaint complaint = new Complaint();

        complaint.setAddressee(addressee);
        complaint.setReason(dto.getReason());
        complaint.setSender(sender);
        complaint.setLocalDateTime(LocalDateTime.now());

        complaintRepository.save(complaint);

        int complaintsNumber = complaintRepository
                .getComplaintsNumberByAddresseeId(addressee.getId());

        if (complaintsNumber >= 100) {
            addressee.setBlocked(true);
        }

        userRepository.save(addressee);

        return "Жалоба была успешно отправлена.";
    }

    public String removeComplaint(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Complaint was not found with ID: "
                                + complaintId)
                );

        User addressee = complaint.getAddressee();

        User sender = complaint.getSender();

        if (!sender.getId().equals(getAuthentication().getId())) {
            throw new ProductViolationException("Вы не можете принять решение по этому продукту, " +
                    "так как он не принадлежить вам.");
        }

        complaintRepository.delete(complaint);

        int complaintsNumber = complaintRepository
                .getComplaintsNumberByAddresseeId(addressee.getId());

        if (complaintsNumber < 100) {
            addressee.setBlocked(false);
        }

        userRepository.save(addressee);

        return "Жалоба была успешно удалена.";
    }

    public List<BlockedUserDto> getBlockedUsers() {
        return userRepository.getBlockedUsers().stream()
                .map(
                        u -> {
                            int complaintsNumber = complaintRepository
                                    .getComplaintsNumberByAddresseeId(u.getId());
                            BlockedUserDto dto = new BlockedUserDto();

                            dto.setUserId(u.getId());
                            dto.setComplaintsNumber(complaintsNumber);
                            dto.setFullName(getUserFullName(u));

                            return dto;
                        }
                )
                .collect(Collectors.toList());
    }

    public List<ComplaintDto> getComplaintsSentFromUser(int offset) {
        User user = getAuthentication();

        return complaintRepository.getComplaintsSentFromUser(user.getId(), offset * 20)
                .stream()
                .map(
                        complaint -> mapToComplaintDto(complaint, complaint.getSender())
                )
                .collect(Collectors.toList());
    }

    public List<ComplaintDto> getComplaintsSentToUser(int offset) {
        User user = getAuthentication();

        return complaintRepository.getComplaintsSentToUser(user.getId(), offset * 20)
                .stream()
                .map(
                        complaint -> mapToComplaintDto(complaint, complaint.getAddressee())
                )
                .collect(Collectors.toList());
    }

    public ComplaintDto mapToComplaintDto(Complaint complaint, User user) {
        ComplaintDto dto = new ComplaintDto();

        dto.setUserFullName(getUserFullName(user));
        dto.setReason(complaint.getReason());
        dto.setLocalDateTime(complaint.getLocalDateTime());

        return dto;
    }

    public String getUserFullName(User user) {
        String fullName = "";

        if(!user.getLastName().isEmpty()) {
            fullName += user.getLastName();
        }
        if(!user.getFirstName().isEmpty()) {
            fullName += user.getFirstName();
        }
        if(!user.getMiddleName().isEmpty()) {
            fullName += user.getMiddleName();
        }

        return fullName;
    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName());
    }

}
