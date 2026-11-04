package fr.efrei.residence;

public class CanalFactory {

    public static CanalNotification creer(Canal canal) {
        return switch (canal) {
            case EMAIL -> new EmailService();
            case SMS -> new SmsService();
            case COURRIER -> new CourrierService();
        };
    }
}
