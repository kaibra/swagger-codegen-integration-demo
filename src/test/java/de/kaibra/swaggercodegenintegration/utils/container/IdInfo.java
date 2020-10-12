package de.kaibra.swaggercodegenintegration.utils.container;

public class IdInfo {
    private final String userId;
    private final String userName;
    private final String groupId;
    private final String groupName;

    public IdInfo(String userId, String userName, String groupId, String groupName) {
        this.userId = userId;
        this.userName = userName;
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String toString() {
        return "IdInfo{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}