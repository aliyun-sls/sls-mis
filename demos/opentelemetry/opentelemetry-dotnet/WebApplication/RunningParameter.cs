using System;
using Grpc.Core;

namespace WebApplication
{
    public interface RunningParameter
    {
        string getProject();

        string getServiceName();

        string getLogstore();

        string getAccessKeyId();

        string getAccessSecret();

        string getEndpoint();
        bool isMissParameter();

        Metadata buildHeader();

        string buildHeaderString();
        string getServiceVersion();
        string getServiceHost();
    }
}