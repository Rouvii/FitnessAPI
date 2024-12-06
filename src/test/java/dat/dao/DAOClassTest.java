package dat.dao;

import dat.config.HibernateConfig;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DAOClassTest {
/*
    private static EntityManagerFactory emf;
    private static DoctorDAO doctorDAO;


    @BeforeAll
    void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        doctorDAO = DoctorDAO.getInstance(emf);
    }

    @BeforeEach
    void setUpEach() {
        Doctor doctor1 = new Doctor("Dr. John", LocalDate.of(1970, 1, 1), 1995, "Clinic A", Speciality.SURGERY);
        Doctor doctor2 = new Doctor("Dr. Jane", LocalDate.of(1980, 2, 2), 2000, "Clinic B", Speciality.GERIATRICS);
        Doctor doctor3 = new Doctor("Dr. Smith", LocalDate.of(1990, 3, 3), 2010, "Clinic C", Speciality.PSYCHIATRY);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(doctor1);
            em.persist(doctor2);
            em.persist(doctor3);
            em.getTransaction().commit();
        }
    }

    @AfterEach
    void tearDownEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Doctor").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void readAll() {
        List<Doctor> doctors = doctorDAO.readAll();
        assertEquals(3, doctors.size());
    }

    @Test
    void read() {
        Doctor doctor = doctorDAO.read(1);
        assertNotNull(doctor);
        assertEquals("Dr. John", doctor.getName());
    }

    @Test
    void create() {
        Doctor doctor = new Doctor("Dr. New", LocalDate.of(2000, 4, 4), 2020, "Clinic D", Speciality.PEDIATRICS);
        doctorDAO.create(doctor);
        assertEquals(4, doctorDAO.readAll().size());
    }

    @Test
    void update() {
        Doctor updatedDoctor = new Doctor("Dr. John", LocalDate.of(1970, 1, 1), 1995, "Clinic A", Speciality.PEDIATRICS);
        doctorDAO.update(1, updatedDoctor);
        Doctor doctor = doctorDAO.read(1);
        assertEquals(Speciality.PEDIATRICS, doctor.getSpeciality());
    }

    @Test
    void doctorBySpeciality() {
        List<Doctor> doctors = doctorDAO.doctorBySpeciality(Speciality.SURGERY);
        assertEquals(1, doctors.size());
        assertEquals("Dr. John", doctors.get(0).getName());
    }

    @Test
    void doctorByBirthdateRange() {
        List<Doctor> doctors = doctorDAO.doctorByBirthdateRange(LocalDate.of(1970, 1, 1), LocalDate.of(1980, 12, 31));
        assertEquals(2, doctors.size());
    }
    */

}