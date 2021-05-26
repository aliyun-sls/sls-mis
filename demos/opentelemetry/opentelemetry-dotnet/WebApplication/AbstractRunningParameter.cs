using System;
using Grpc.Core;

namespace WebApplication
{
    public abstract class AbstractRunningParameter : RunningParameter
    {
        public abstract string getProject();

        public abstract string getServiceName();

        public abstract string GetInstance();

        public abstract string getAccessKeyId();

        public abstract string getAccessSecret();

        public abstract string getEndpoint();

        public bool isMissParameter()
        {
            return string.IsNullOrEmpty(getProject()) || string.IsNullOrEmpty(GetInstance()) ||
                   string.IsNullOrEmpty(getEndpoint()) ||
                   string.IsNullOrEmpty(getAccessKeyId()) || string.IsNullOrEmpty(getAccessSecret());
        }

        public Metadata buildHeader()
        {
            return new Metadata
            {
                {"x-sls-otel-project", getProject()},
                {"x-sls-otel-instance-id", GetInstance()},
                {"x-sls-otel-ak-id", getAccessKeyId()},
                {"x-sls-otel-ak-secret", getAccessSecret()}
            };
        }

        public string buildHeaderString()
        {
            return String.Join(",", new string?[]
                {
                    String.Format("x-sls-otel-project={0}", getProject()),
                    String.Format("x-sls-otel-instance-id={0}", GetInstance()),
                    String.Format("x-sls-otel-ak-id={0}", getAccessKeyId()),
                    String.Format("x-sls-otel-ak-secret={0}", getAccessSecret()),
                }
            );
        }


        public abstract string getServiceVersion();

        public abstract string getServiceHost();
    }
}