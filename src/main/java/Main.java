import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.database.datasets.DatasetIdentifier;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import server.network.clienthandling.logicutils.messaging.MessengerViewUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.*;
import shareables.models.pojos.media.Picture;
import shareables.models.pojos.messaging.Conversation;
import shareables.models.pojos.messaging.Message;
import shareables.models.pojos.messaging.MessageType;
import shareables.models.pojos.unitselection.UnitSelectionSession;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.professors.AcademicRole;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.specialusers.Admin;
import shareables.models.pojos.users.specialusers.MrMohseni;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;
import server.database.management.DatabaseManager;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigIdSupplier;
import shareables.utils.config.ConfigManager;
import shareables.utils.objectmapping.ObjectMapperUtils;
import shareables.utils.timing.timekeeping.DayTime;
import shareables.utils.timing.timekeeping.WeekTime;
import shareables.utils.timing.timekeeping.Weekday;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final DatabaseManager manager = new DatabaseManager();
    private static Admin admin;

    public static void main(String[] args) {
        ConfigIdSupplier.resetCurrentClientId();
        manager.getDatabaseWriter().purgeDirectory(new File(ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "datasetsFolderPath")));
        manager.getDatabaseWriter().purgeDirectoryCompletely(new File(ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "localDatasetsFolderPath")));
        createTestData(); // TODO: cleaning the directories
        sendAdminHelpMessages();
//        manager.loadDatabase();
//        Student student = (Student) manager.get(DatasetIdentifier.STUDENTS, "19101100");
//        System.out.println(student);
    }

    private static void sendAdminHelpMessages() {
        List<Identifiable> allUsers = new ArrayList<>();
        allUsers.addAll(manager.getIdentifiables(DatasetIdentifier.PROFESSORS));
        allUsers.addAll(manager.getIdentifiables(DatasetIdentifier.STUDENTS));
        allUsers.add(manager.get(DatasetIdentifier.SPECIAL_USERS, ConfigManager.getString(ConfigFileIdentifier.CONSTANTS,
                "mrMohseniId")));

        String messageText = ConfigManager.getString(ConfigFileIdentifier.TEXTS, "adminHelpingPrompt");
        String adminId = admin.getId();
        allUsers.forEach(user -> {
            Message message = new Message();
            message.setSenderId(adminId);
            message.setMessageType(MessageType.TEXT);
            message.setMessageText(messageText);
            addMessageToSenderAndReceiversConversations(manager, adminId, new ArrayList<>(List.of(user.getId())), message);
        });

        manager.saveDatabase();
    }

    private static void createTestData() {
        Department mathDepartment = new Department(DepartmentName.MATHEMATICS);
        Professor khazayi = new Professor(AcademicRole.DEPUTY, AcademicLevel.ASSOCIATE, mathDepartment.getId());
        khazayi.setNationalId("015033903");
        khazayi.setFirstName("Shahram");
        khazayi.setLastName("Khazayi");
        khazayi.setPhoneNumber("09129730021");
        khazayi.setEmailAddress("khazayi@sharif.edu");
        khazayi.setOfficeNumber("105");
        khazayi.setPassword("1234");
        Student hamidi = new Student(2019, DegreeLevel.UNDERGRADUATE, mathDepartment.getId());
        hamidi.setNationalId("0150802202");
        hamidi.setFirstName("Aref");
        hamidi.setLastName("Hamidi");
        hamidi.setPhoneNumber("09192302103");
        hamidi.setEmailAddress("hamidi@sharif.edu");
        hamidi.setAdvisingProfessorId(khazayi.getId());
        hamidi.setPassword("1234");
        hamidi.updateLastLogin();
        hamidi.setEnrolmentTime(LocalDateTime.now());
        khazayi.addToAdviseeStudentIds(hamidi.getId());
        mathDepartment.setDeputyId(khazayi.getId());
        mathDepartment.addToStudentIds(hamidi.getId());
        Professor fanaei = new Professor(AcademicRole.DEAN, AcademicLevel.FULL, mathDepartment.getId());
        fanaei.setNationalId("015033904");
        fanaei.setFirstName("Hamidreza");
        fanaei.setLastName("Fanaei");
        fanaei.setPhoneNumber("09129730321");
        fanaei.setEmailAddress("fanaei@sharif.edu");
        fanaei.setOfficeNumber("107");
        fanaei.setPassword("1234");
        mathDepartment.setDeanId(fanaei.getId());
        mathDepartment.addToProfessorIds(khazayi.getId());
        mathDepartment.addToProfessorIds(fanaei.getId());
        Student rezaei = new Student(2018, DegreeLevel.GRADUATE, mathDepartment.getId());
        rezaei.setNationalId("0152902202");
        rezaei.setFirstName("Arash");
        rezaei.setLastName("Rezaei");
        rezaei.setPhoneNumber("09192302110");
        rezaei.setEmailAddress("rezaei@sharif.edu");
        rezaei.setPassword("5678");
        mathDepartment.addToStudentIds(rezaei.getId());
        Course complexAnalysis = new Course(mathDepartment.getId(), new TermIdentifier(2022, 2), 1);
        complexAnalysis.setCourseName("Complex Analysis");
        complexAnalysis.addToTeachingProfessorIds(khazayi.getId());
        complexAnalysis.addToTeachingProfessorIds(fanaei.getId());
        complexAnalysis.addToStudentIds(hamidi.getId());
        complexAnalysis.addToStudentIds(rezaei.getId());
//        complexAnalysis.addToTAs(rezaei);
        complexAnalysis.setNumberOfCredits(4);
//        complexAnalysis.setActive(true);
        complexAnalysis.setDegreeLevel(DegreeLevel.GRADUATE);
        complexAnalysis.setExamDate(LocalDateTime.of(2022, 11, 21, 9, 30));
        WeekTime firstWeekTime = new WeekTime(Weekday.SUNDAY, new DayTime(14, 30, 0),
                new DayTime(16, 30, 0));
        WeekTime secondWeekTime = new WeekTime(Weekday.TUESDAY, new DayTime(14, 30, 0),
                new DayTime(16, 30, 0));
        complexAnalysis.addToWeeklyClassTimes(firstWeekTime);
        complexAnalysis.addToWeeklyClassTimes(secondWeekTime);
        complexAnalysis.setCourseCapacity(30);
//        hamidi.getTranscript().put(complexAnalysis.getId(), new Score(false, 20.0));
//        hamidi.getTranscript().setGPA(20.0);
        // TODO: being careful not to add a student twice to a course in course picking stuff
        mathDepartment.addToCourseIDs(complexAnalysis.getId());
        Course realAnalysis = new Course(mathDepartment.getId(), new TermIdentifier(2022, 2), 1);
        realAnalysis.setCourseName("A Real Analysis");
        realAnalysis.addToTeachingProfessorIds(fanaei.getId());
        realAnalysis.addToStudentIds(hamidi.getId());
        realAnalysis.addToTeachingAssistantIds(rezaei.getId());
        realAnalysis.setNumberOfCredits(3);
        realAnalysis.setDegreeLevel(DegreeLevel.UNDERGRADUATE);
        realAnalysis.setExamDate(LocalDateTime.of(2022, 9, 21, 9, 30));
        realAnalysis.addToWeeklyClassTimes(firstWeekTime);
        realAnalysis.addToWeeklyClassTimes(secondWeekTime);
        realAnalysis.setCourseCapacity(40);
        mathDepartment.addToCourseIDs(realAnalysis.getId());

        UnitSelectionSession session = new UnitSelectionSession();
        session.setStartsAt(LocalDateTime.now());
        session.setEndsAt(LocalDateTime.of(2023, 10, 11, 10, 30));
        session.setIntendedDegreeLevel(DegreeLevel.UNDERGRADUATE);
        session.setIntendedYearOfEntry(2019);
        mathDepartment.addToUnitSelectionSessions(session);

        Department physicsDepartment = new Department(DepartmentName.PHYSICS);
        Professor rahvar = new Professor(AcademicRole.DEPUTY, AcademicLevel.FULL, physicsDepartment.getId());
        rahvar.setNationalId("015053903");
        rahvar.setFirstName("Sohrab");
        rahvar.setLastName("Rahvar");
        rahvar.setPhoneNumber("09125730021");
        rahvar.setEmailAddress("rahvar@sharif.edu");
        rahvar.setOfficeNumber("205");
        rahvar.setPassword("1234");
        physicsDepartment.setDeputyId(rahvar.getId());
        physicsDepartment.addToProfessorIds(rahvar.getId());
        Course analyticalMechanics = new Course(physicsDepartment.getId(), new TermIdentifier(2022, 2), 1);
        analyticalMechanics.setDegreeLevel(DegreeLevel.UNDERGRADUATE);
        analyticalMechanics.setCourseName("Analytical Mechanics");
        analyticalMechanics.addToTeachingProfessorIds(rahvar.getId());
        analyticalMechanics.addToStudentIds(hamidi.getId());
        analyticalMechanics.addToTeachingAssistantIds(rezaei.getId());
        analyticalMechanics.setNumberOfCredits(3);
        analyticalMechanics.setExamDate(LocalDateTime.of(2022, 11, 21, 12, 30));
        analyticalMechanics.addToWeeklyClassTimes(firstWeekTime);
        analyticalMechanics.addToWeeklyClassTimes(secondWeekTime);
        analyticalMechanics.addToStudentIds(rezaei.getId());
        physicsDepartment.addToCourseIDs(analyticalMechanics.getId());
//        hamidi.getTranscript().put(analyticalMechanics.getId(), new Score(false, 19.0));

        Conversation conversation = new Conversation();
        conversation.addToConversingUserIds(hamidi.getId());
        conversation.addToConversingUserIds(rezaei.getId());
        Message firstMessage = new Message();
        firstMessage.setMessageType(MessageType.TEXT);
        firstMessage.setMessageText("Certainly sir there is much surrounding this problem");
        firstMessage.setSenderId(rezaei.getId());
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message secondMessage = new Message();
        secondMessage.setMessageType(MessageType.MEDIA);
        secondMessage.setMessageMediaFile(new Picture(true));
        secondMessage.setSenderId(hamidi.getId());
        conversation.addToMessages(firstMessage);
        conversation.addToMessages(secondMessage);
        hamidi.getMessenger().addToConversations(conversation);
        rezaei.getMessenger().addToConversations(conversation);

        Conversation anotherConversation = new Conversation();
        anotherConversation.addToConversingUserIds(hamidi.getId());
        anotherConversation.addToConversingUserIds(khazayi.getId());
        Message anotherFirstMessage = new Message();
        anotherFirstMessage.setMessageType(MessageType.TEXT);
        anotherFirstMessage.setMessageText("Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo  Yo Yo Yo Yo Yo Yo Yo Yo Yo Yo ");
        anotherFirstMessage.setSenderId(khazayi.getId());
        anotherConversation.addToMessages(anotherFirstMessage);
        hamidi.getMessenger().addToConversations(anotherConversation);
        khazayi.getMessenger().addToConversations(anotherConversation);

        admin = new Admin();
        MrMohseni mrMohseni = new MrMohseni();

        Department generalCenters = new Department(DepartmentName.GENERAL_CENTERS);
        Professor movahed = new Professor(AcademicRole.DEAN, AcademicLevel.FULL, "0");
        movahed.setNationalId("015053903");
        movahed.setFirstName("Ali");
        movahed.setLastName("Movahed");
        movahed.setPhoneNumber("09125730021");
        movahed.setEmailAddress("movahed@sharif.edu");
        movahed.setOfficeNumber("005");
        movahed.setPassword("1234");
        generalCenters.addToProfessorIds(movahed.getId());
        generalCenters.setDeanId(movahed.getId());
        Professor keyvani = new Professor(AcademicRole.DEPUTY, AcademicLevel.FULL, "0");
        keyvani.setNationalId("015053903");
        keyvani.setFirstName("Mohammad");
        keyvani.setLastName("Keyvani");
        keyvani.setPhoneNumber("09125730021");
        keyvani.setEmailAddress("keyvani@sharif.edu");
        keyvani.setOfficeNumber("006");
        keyvani.setPassword("1234");
        generalCenters.addToProfessorIds(keyvani.getId());
        generalCenters.setDeputyId(keyvani.getId());

        manager.save(DatasetIdentifier.STUDENTS, hamidi);
        manager.save(DatasetIdentifier.STUDENTS, rezaei);
        manager.save(DatasetIdentifier.PROFESSORS, khazayi);
        manager.save(DatasetIdentifier.PROFESSORS, fanaei);
        manager.save(DatasetIdentifier.COURSES, complexAnalysis);
        manager.save(DatasetIdentifier.COURSES, realAnalysis);
        manager.save(DatasetIdentifier.COURSES, analyticalMechanics);
        manager.save(DatasetIdentifier.DEPARTMENTS, mathDepartment);
        manager.save(DatasetIdentifier.PROFESSORS, rahvar);
        manager.save(DatasetIdentifier.DEPARTMENTS, physicsDepartment);
        manager.save(DatasetIdentifier.SPECIAL_USERS, admin);
        manager.save(DatasetIdentifier.SPECIAL_USERS, mrMohseni);
        manager.save(DatasetIdentifier.DEPARTMENTS, generalCenters);
        manager.save(DatasetIdentifier.PROFESSORS, movahed);
        manager.save(DatasetIdentifier.PROFESSORS, keyvani);
        manager.saveDatabase();
    }

    private static void addMessageToSenderAndReceiversConversations(DatabaseManager databaseManager, String senderId,
                                                                    List<String> receiverIds, Message message) {
        User sender = IdentifiableFetchingUtils.getUser(databaseManager, senderId);
        for (String receiverId : receiverIds) {
            Conversation receiverConversation = MessengerViewUtils.getContactConversation(databaseManager, receiverId, senderId);
            if (receiverConversation == null) {
                receiverConversation = createNewConversation(senderId, receiverId);
                receiverConversation.addToMessages(message);

                User receiver = IdentifiableFetchingUtils.getUser(databaseManager, receiverId);
                receiver.getMessenger().addToConversations(receiverConversation);

                Conversation senderConversation = copyConversation(receiverConversation);
                sender.getMessenger().addToConversations(senderConversation);
            } else {
                receiverConversation.addToMessages(message);

                Conversation senderConversation = MessengerViewUtils.getContactConversation(databaseManager, senderId,
                        receiverId);
                senderConversation.addToMessages(message);
            }
        }
    }

    private static Conversation copyConversation(Conversation conversation) {
        ObjectMapper objectMapper = ObjectMapperUtils.getNetworkObjectMapper();
        String conversationJson = null;
        try {
            conversationJson = objectMapper.writeValueAsString(conversation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (conversationJson == null) return null;

        Conversation copiedConversation = null;
        try {
            copiedConversation = objectMapper.readValue(conversationJson, Conversation.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (copiedConversation == null) return null;

        copiedConversation.initializeId(Conversation.getSequentialIdGenerator());
        return copiedConversation;
    }

    private static Conversation createNewConversation(String senderId, String receiverId) {
        Conversation conversation = new Conversation();
        conversation.addToConversingUserIds(senderId);
        conversation.addToConversingUserIds(receiverId);

        return conversation;
    }
}