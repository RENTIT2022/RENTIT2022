package kg.neobis.rentit.enums;

public enum BookingStatus {

    ACCEPTED("Одобрено"),
    REJECTED("Отклонено"),
    PENDING("В ожидании");

    private final String status;

    BookingStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
