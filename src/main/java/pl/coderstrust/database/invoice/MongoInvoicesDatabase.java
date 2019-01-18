package pl.coderstrust.database.invoice;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import pl.coderstrust.configuration.MongoRepositoryProperties;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "mongodb")
@Repository
public class MongoInvoicesDatabase implements InvoiceDatabase {

  private final MongoRepositoryProperties properties;
  private MongoTemplate mongoTemplate;

  @Autowired
  public MongoInvoicesDatabase(@NonNull MongoTemplate mongoTemplate, @NonNull MongoRepositoryProperties properties) {
    this.mongoTemplate = mongoTemplate;
    this.properties = properties;
  }

  @Override
  public Iterable<Invoice> findAllBySellerName(String sellerName) throws DatabaseOperationException {
    return null;
  }

  @Override
  public Iterable<Invoice> findAllByBuyerName(String buyerName) throws DatabaseOperationException {
    return null;
  }

  @Override
  public Invoice save(@NonNull Invoice invoice) {
    mongoTemplate.insert(invoice, properties.getCollectionName());
    return invoice;
  }

  @Override
  public Optional<Invoice> findById(String var1) throws DatabaseOperationException {
    return Optional.empty();
  }

  @Override
  public boolean existsById(String var1) throws DatabaseOperationException {
    return false;
  }

  @Override
  public Iterable<Invoice> findAll() throws DatabaseOperationException {
    return mongoTemplate.findAll(Invoice.class, properties.getCollectionName());
  }

  @Override
  public long count() throws DatabaseOperationException {
    return 0;
  }

  @Override
  public void deleteById(String var1) throws DatabaseOperationException {

  }

  @Override
  public void deleteAll() throws DatabaseOperationException {

  }
}
