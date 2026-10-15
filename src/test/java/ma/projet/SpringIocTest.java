package ma.projet;
import org.junit.Test;import static org.junit.Assert.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ma.projet.config.EvaluationConfig;import ma.projet.service.*;import ma.projet.util.HibernateUtil;
import org.hibernate.SessionFactory;
/** Vérifie le câblage IoC puis une vraie lecture sur MySQL. */
public class SpringIocTest {
    @Test public void servicesInjectesEtFactoryCommune() {
        try(var context=new AnnotationConfigApplicationContext(EvaluationConfig.class)) {
            SessionFactory factory=context.getBean(SessionFactory.class);
            assertSame(HibernateUtil.getSessionFactory(),factory);
            assertSame(context.getBean(ProduitService.class),context.getBean(ProduitService.class));
            var data=DemoData.seed(factory);
            assertEquals(3,context.getBean(ProduitService.class).parCategorie(data.categorie().getId()).size());
            assertEquals(10,context.getBean(FemmeService.class).findAll().size());
            try(var session=factory.openSession()){session.doWork(connection->assertEquals("MySQL",connection.getMetaData().getDatabaseProductName()));}
        }
    }
}
