package ucab.ingsw.command;

public interface ValidationRules {
    int EMAIL_MIN_SIZE = 12;
    int PASSWORD_MIN_SIZE = 8;
    int FIRST_LAST_NAME_MAX_SIZE = 40;
    int NAME_MAX_SIZE = 40;
    String FIRST_LAST_NAME_REGEX = "[a-zA-Z ]([&.'-]?[a-zA-Z ][&.'-]?)*";
}
