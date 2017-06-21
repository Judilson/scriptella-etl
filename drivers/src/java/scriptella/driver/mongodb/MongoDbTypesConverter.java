package scriptella.driver.mongodb;

import scriptella.util.IOUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Converter for objects passed to and from MongoDB.
 * <p>
 * Some types link {@link Clob}/{@link Blob} needs conversion before being passed to MongoDB.</p>
 * Some types link {@link BigDecimal} needs conversion before being passed to MongoDB.</p>
 *
 * @author Fyodor Kupolov
 * @author Judilson Costa
 * @version 1.1
 */
public class MongoDbTypesConverter {

    //Since BLOBS/CLOBS are stored in memory, use a reasonable default limit of 64MB
    static long MAX_LOB_SIZE = Long.getLong("MongoDbTypesConverter.MAX_LOB_SIZE", 64 * 1024 * 1024);

    public Object toMongoDb(Object object) {
        if (object instanceof Clob) {
            try {
                return IOUtils.toString(((Clob) object).getCharacterStream(), MAX_LOB_SIZE);
            } catch (IOException e) {
                throw new MongoDbProviderException("Cannot convert clob to string", e);
            } catch (SQLException e) {
                throw new MongoDbProviderException("Cannot convert clob to string", e);
            }
        }
        if (object instanceof Blob) {
            try {
                return IOUtils.toByteArray(((Blob) object).getBinaryStream(), MAX_LOB_SIZE);
            } catch (IOException e) {
                throw new MongoDbProviderException("Cannot convert clob to string", e);
            } catch (SQLException e) {
                throw new MongoDbProviderException("Cannot convert clob to string", e);
            }
        }
        if (object instanceof BigDecimal) {
            try {
                return ((BigDecimal) object).doubleValue();
            } catch (Exception e) {
                throw new MongoDbProviderException("Cannot convert BigDecimal to Double", e);
            }
        }

        return object;
    }
}
