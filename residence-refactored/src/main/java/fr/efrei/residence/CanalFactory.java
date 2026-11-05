package fr.efrei.residence;

public class CanalFactory {

    private final EmailService email;
    private final SmsService sms;
    private final CourrierService courrier;

    public CanalFactory(EmailService email, SmsService sms, CourrierService courrier) {
        this.email = email;
        this.sms = sms;
        this.courrier = courrier;
    }

    public CanalNotification creer(Canal canal) {
        return switch (canal) {
            case EMAIL -> email;
            case SMS -> sms;
            case COURRIER -> courrier;
        };
    }
}
