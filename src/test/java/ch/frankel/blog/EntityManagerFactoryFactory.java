package ch.frankel.blog;

import org.h2.Driver;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;

import static org.hibernate.cfg.AvailableSettings.*;

public class EntityManagerFactoryFactory {

    private static EntityManagerFactory factory;

    private static Map<String, Object> properties() {
        return Map.of(JPA_JDBC_DRIVER, Driver.class.getName(),
                JPA_JDBC_URL, "jdbc:h2:mem:testdb",
                JPA_JDBC_USER, "sa",
                DIALECT, H2Dialect.class,
                HBM2DDL_AUTO, "create-only",
                SHOW_SQL, true);
    }

    public EntityManager getEntityManager() {
        if (factory == null) {
            factory = new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                    new TestPersistenceUnitInfo(),
                    properties());
        }
        return factory.createEntityManager();
    }
}

class TestPersistenceUnitInfo implements PersistenceUnitInfo {

    @Override
    public String getPersistenceUnitName() {
        return "TestPersistenceUnit";
    }

    @Override
    public String getPersistenceProviderClassName() {
        return null;
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    @Override
    public DataSource getJtaDataSource() {
        return null;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return null;
    }

    @Override
    public List<String> getMappingFileNames() {
        return Collections.emptyList();
    }

    @Override
    public List<URL> getJarFileUrls() {
        try {
            return Collections.list(getClass()
                    .getClassLoader()
                    .getResources(""));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    @Override
    public List<String> getManagedClassNames() {
        return new ArrayList<>();
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return false;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return null;
    }

    @Override
    public ValidationMode getValidationMode() {
        return null;
    }

    @Override
    public Properties getProperties() {
        return new Properties();
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {}

    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }
}