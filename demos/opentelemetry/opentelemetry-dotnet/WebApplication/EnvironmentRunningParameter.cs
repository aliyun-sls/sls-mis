using System;

namespace WebApplication
{
    public class EnvironmentRunningParameter : AbstractRunningParameter
    {
        private readonly string project;
        private readonly string instance;
        private readonly string endpoint;
        private readonly string accessKeyId;
        private readonly string accessSecret;
        private readonly string serviceName;
        private readonly string? serviceVersion;
        private readonly string? serviceHost;

        public EnvironmentRunningParameter()
        {
            project = Environment.GetEnvironmentVariable("PROJECT");
            instance = Environment.GetEnvironmentVariable("INSTANCE");
            endpoint = Environment.GetEnvironmentVariable("ENDPOINT");
            accessKeyId = Environment.GetEnvironmentVariable("ACCESS_KEY_ID");
            accessSecret = Environment.GetEnvironmentVariable("ACCESS_SECRET");
            serviceName = Environment.GetEnvironmentVariable("SERVICE_NAME");
            serviceVersion = Environment.GetEnvironmentVariable("SERVICE_VERSION");
            serviceHost = Environment.GetEnvironmentVariable("SERVICE_HOST");

            if (string.IsNullOrEmpty(serviceName))
            {
                serviceName = "opentelemetry-donet";
            }

            if (string.IsNullOrEmpty(serviceVersion))
            {
                serviceVersion = "1.1.0-beta2";
            }
        }

        public override string getServiceVersion()
        {
            return serviceVersion;
        }

        public override string getServiceHost()
        {
            return serviceHost;
        }

        public override string getProject()
        {
            return project;
        }

        public override string getServiceName()
        {
            return serviceName;
        }

        public override string GetInstance()
        {
            return instance;
        }

        public override string getAccessKeyId()
        {
            return accessKeyId;
        }

        public override string getAccessSecret()
        {
            return accessSecret;
        }

        public override string getEndpoint()
        {
            return endpoint;
        }
    }
}
