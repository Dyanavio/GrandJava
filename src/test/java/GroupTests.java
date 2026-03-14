import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.example.Group;
import org.example.Student;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

public class GroupTests
{
    private Group group;
    private static final LocalDate VALID_DATE = LocalDate.of(206, 11, 20);
    Student experimentStudent = new Student("Aqua Hoshino", VALID_DATE, Arrays.asList(5, 5, 5), Arrays.asList(5, 4, 5));


    @BeforeEach
    void SetUp()
    {
        group = new Group("Class A");
        group.addStudent(new Student("Akane Kurokawa", VALID_DATE, Arrays.asList(4, 5, 5), Arrays.asList(5, 5, 5)));
    }

    @Test
    @DisplayName("Initial Group creation check")
    void testGroupCreation()
    {
        group = new Group("Class A");
        assertAll("Group Initialization",
                () -> assertEquals("Class A", group.getGroupName(), "Group name should match the expected one"),
                () -> assertEquals(0, group.getStudentCount(), "Student's count in a group must be zero"));
    }

    @Test
    @DisplayName("Student addition check")
    void testStudentAddition()
    {
        group.addStudent(experimentStudent);

        assertAll("Adding a student",
                () -> assertFalse(group.isEmpty(), "Group must not be empty now"),
                () -> assertEquals(2, group.getStudents().size(), "Group size called with getStudents().size() must be 1"),
                () -> assertEquals(2, group.getStudentCount(), "Group size called with getStudentCount() must be 1"));
    }

    @Test
    @DisplayName("Null values check")
    void testNullValues()
    {
        assertAll("Null values tests",
                () -> assertThrows(IllegalArgumentException.class, () -> new Group(null), "Group must throw if the passed name is null"),
                () -> assertThrows(IllegalArgumentException.class, () -> group.addStudent(null), "Null cannot be added to the group"));
    }

    @Test
    @DisplayName("Student removal check")
    void testStudentRemoval()
    {
        group.removeStudentByName("Akane Kurokawa");
        assertAll("Removing a student",
                () -> assertThrows(IllegalArgumentException.class, () -> group.removeStudent(null), "Cannot remove null reference"),
                () -> assertThrows(IllegalArgumentException.class, () -> group.removeStudentByName(null), "Cannot remove by null name"),
                () -> assertThrows(IllegalArgumentException.class, () -> group.removeStudentByName(""), "Canno remove by '' name"),
                () -> assertTrue(group.isEmpty(), "Group must not have any students"),
                () -> assertEquals(0, group.getStudentCount(), "Group size called with getStudentCount() must be zero"),
                () -> assertEquals(0, group.getStudents().size(), "Group size called with getStudents().size() must be zero"));
    }

    @Test
    @DisplayName("Sort check")
    void testSortByGrades()
    {
        group.addStudent(experimentStudent);
        group.addStudent(new Student("Arima Kana", VALID_DATE));
        int sizeBeforeSort = group.getStudentCount();

        List<Student> sortedByExam = group.getStudentsSortedByAverageExamGrade();

        assertAll("Exam Sort check",
                () -> assertEquals(sizeBeforeSort, sortedByExam.size(), "Size must remain the same after sort"),
                () -> assertEquals("Akane Kurokawa", sortedByExam.getFirst().getFullName(), "Student with specified name must be the first element"),
                () -> assertEquals("Arima Kana", sortedByExam.get(sizeBeforeSort - 1).getFullName(), "Student with specified name must be the last element"));

        List<Student> sortedByHomework = group.getStudentsSortedByAverageHomeworkGrade();
        assertAll("Homework Sort check",
                () -> assertEquals(sizeBeforeSort, sortedByHomework.size(), "Size must remain the same after sort"),
                () -> assertEquals("Aqua Hoshino", sortedByHomework.getFirst().getFullName(), "Student with specified name must be the first element"),
                () -> assertEquals("Arima Kana", sortedByHomework.get(sizeBeforeSort - 1).getFullName(), "Student with specified name must be the last element"));

    }

    @Test
    @DisplayName("Average values check")
    void testAverageValues()
    {
        double averageByHomework = (4 + 5 + 5) / 3.0;
        double averageByExam = (5 + 5 + 5) / 3.0;

        assertAll("Average values for exams and homeworks",
                () -> assertEquals(averageByExam, group.getAverageExamGrade(), "Average values for exams must be equal"),
                () -> assertEquals(averageByHomework, group.getAverageHomeworkGrade(), "Average values for homework must be equal"));
    }







}
