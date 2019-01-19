package pl.coderstrust.database.invoice;

import java.util.Optional;
import lombok.NonNull;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pl.coderstrust.configuration.MongoDatabaseProperties;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "mongodb")
@Repository
public class MongoInvoicesDatabase implements InvoiceDatabase {

  private final MongoDatabaseProperties properties;
  private MongoTemplate mongoTemplate;

  @Autowired
  public MongoInvoicesDatabase(@NonNull MongoTemplate mongoTemplate, @NonNull MongoDatabaseProperties properties) {
    this.mongoTemplate = mongoTemplate;
    this.properties = properties;
  }

  @Override
  public Iterable<Invoice> findAllBySellerName(String sellerName) throws DatabaseOperationException {
    return mongoTemplate.find(Query.query(Criteria.where("sellerName").is(sellerName)), Invoice.class, properties.getCollectionName());
  }

  @Override
  public Iterable<Invoice> findAllByBuyerName(String buyerName) throws DatabaseOperationException {
    return mongoTemplate.find(Query.query(Criteria.where("buyerName").is(buyerName)), Invoice.class, properties.getCollectionName());
  }

  @Override
  public Invoice save(@NonNull Invoice invoice) {
    System.out.println("\n\nMongodb query findById\n");
    mongoTemplate.save(invoice, properties.getCollectionName());
    return invoice;
  }

  @Override
  public Optional<Invoice> findById(String id) throws DatabaseOperationException {
    System.out.println("\n\nMongodb query findById\n");
    return Optional.ofNullable(mongoTemplate.findById(id, Invoice.class, properties.getCollectionName()));
  }

  @Override
  public boolean existsById(String id) throws DatabaseOperationException {
    System.out.println("\n\nMongodb query existsById\n");
    return mongoTemplate.exists(Query.query(Criteria.where("_id").is(id)), Invoice.class, properties.getCollectionName());
  }

  @Override
  public Iterable<Invoice> findAll() throws DatabaseOperationException {
    System.out.println("\n\nMongodb query findAll\n");
    return mongoTemplate.findAll(Invoice.class, properties.getCollectionName());
  }

  @Override
  public long count() throws DatabaseOperationException {
    System.out.println("\n\nMongodb query count\n");
    return mongoTemplate.getCollection(properties.getCollectionName()).count();
  }

  @Override
  public void deleteById(String id) throws DatabaseOperationException {
    System.out.println("\n\nMongodb query deleteById\n");
    mongoTemplate.findAndRemove(Query.query(Criteria.where("_id").is(id)), Invoice.class, properties.getCollectionName());
  }

  @Override
  public void deleteAll() throws DatabaseOperationException {
    System.out.println("\n\nMongodb query deleteAll\n");
    mongoTemplate.getCollection(properties.getCollectionName()).deleteMany(new Document());
  }
}
