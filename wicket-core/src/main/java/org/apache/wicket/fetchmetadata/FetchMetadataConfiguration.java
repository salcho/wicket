package org.apache.wicket.fetchmetadata;

public class FetchMetadataConfiguration {

  enum FetchMetadataMode {
    REPORTING,
    ENFORCE
  }

  final FetchMetadataMode mode;
  final String[] exemptions;
  final FetchMetadataLoggingConfiguration loggingConfiguration;
  final ResourceIsolationPolicy[] policies;

  private FetchMetadataConfiguration(
      FetchMetadataMode mode, String[] exemptions,
      FetchMetadataLoggingConfiguration loggingConfiguration,
      ResourceIsolationPolicy[] policies) {
    this.mode = mode;
    this.exemptions = exemptions;
    this.loggingConfiguration = loggingConfiguration;
    this.policies = policies;
  }

  public static class Builder {
    private FetchMetadataMode mode;
    private String[] exemptions;
    private FetchMetadataLoggingConfiguration loggingConfiguration;
    private ResourceIsolationPolicy[] policies = new ResourceIsolationPolicy[] {
        new DefaultResourceIsolationPolicy()
    };

    Builder withMode(FetchMetadataMode mode) {
      this.mode = mode;
      return this;
    }

    Builder withExemptions(String... exemptions) {
      this.exemptions = exemptions;
        return this;
    }

    Builder withLoggingConfiguration(FetchMetadataLoggingConfiguration loggingConfiguration) {
      this.loggingConfiguration = loggingConfiguration;
        return this;
    }

    Builder withResourceIsolationPolicies(ResourceIsolationPolicy... policies) {
      this.policies = policies;
      return this;
    }

    FetchMetadataConfiguration build() {
      return new FetchMetadataConfiguration(mode, exemptions, loggingConfiguration, policies);
    }
  }
}
