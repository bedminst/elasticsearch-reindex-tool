package pl.allegro.tech.search.elasticsearch.tools.reindex;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import pl.allegro.tech.search.elasticsearch.tools.reindex.command.ReindexCommand;
import pl.allegro.tech.search.elasticsearch.tools.reindex.connection.ElasticDataPointer;
import pl.allegro.tech.search.elasticsearch.tools.reindex.connection.ParsingElasticsearchAddressException;
import pl.allegro.tech.search.elasticsearch.tools.reindex.query.QuerySegmentationFactory;
import pl.allegro.tech.search.elasticsearch.tools.reindex.connection.ElasticDataPointerBuilder;
import pl.allegro.tech.search.elasticsearch.tools.reindex.query.QuerySegmentation;

public class ReindexCommandParser {

  private ElasticDataPointer sourcePointer;
  private ElasticDataPointer targetPointer;
  private QuerySegmentation segmentation;


  public boolean tryParse(String... args) {
    ReindexCommand command = new ReindexCommand();
    JCommander jCommander = new JCommander(command);

    try {
      jCommander.parse(args);
      buildReindexParameters(command);

    } catch (ParameterException | ParsingElasticsearchAddressException exception) {
      jCommander.usage();
      return false;
    }
    return true;
  }

  private void buildReindexParameters(ReindexCommand command) {
    sourcePointer = ElasticDataPointerBuilder.builder()
        .setClusterName(command.getSourceClusterName())
        .setAddress(command.getSource())
        .build();
    targetPointer = ElasticDataPointerBuilder.builder()
        .setClusterName(command.getTargetClusterName())
        .setAddress(command.getTarget())
        .build();
    segmentation = getFieldSegmentation(command);
  }

  private QuerySegmentation getFieldSegmentation(ReindexCommand command) {
    return QuerySegmentationFactory.create(command);
  }

  public ElasticDataPointer getSourcePointer() {
    return sourcePointer;
  }

  public ElasticDataPointer getTargetPointer() {
    return targetPointer;
  }

  public QuerySegmentation getSegmentation() {
    return segmentation;
  }
}
