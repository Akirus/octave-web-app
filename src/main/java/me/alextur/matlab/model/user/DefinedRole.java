package me.alextur.matlab.model.user;

/**
 * @author Alex Turchynovich
 */
public enum DefinedRole {

    User("User"), Admin("Admin"), Teacher("Teacher");

    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String pRoleName) {
        roleName = pRoleName;
    }

    DefinedRole(String pRoleName) {
        roleName = pRoleName;
    }
}
