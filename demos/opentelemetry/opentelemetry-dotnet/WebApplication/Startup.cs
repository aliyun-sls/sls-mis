using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Grpc.Core;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using OpenTelemetry;
using OpenTelemetry.Resources;
using OpenTelemetry.Trace;

namespace WebApplication
{
    public class Startup
    {
        public void ConfigureServices(IServiceCollection services)
        {
            RunningParameter runningParameter = new EnvironmentRunningParameter();
            if (runningParameter.isMissParameter())
            {
                Console.WriteLine("Miss parameters");
                Environment.Exit(-1);
            }

            services.AddOpenTelemetryTracing(
                (builder) => builder
                    .AddAspNetCoreInstrumentation()
                    .AddOtlpExporter(options =>
                    {
                        options.Endpoint = runningParameter.getEndpoint();
                        options.Headers = runningParameter.buildHeader();
                        options.Credentials = new SslCredentials();
                    })
                    .SetResourceBuilder(ResourceBuilder.CreateDefault()
                        .AddAttributes(new Dictionary<string, object>
                        {
                            {"service.name", runningParameter.getServiceName()},
                            {"service.version", runningParameter.getServiceVersion()},
                            {"service.host", runningParameter.getServiceHost()}
                        }))
            );
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseRouting();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapGet("/hello-world",
                    async context => { await context.Response.WriteAsync("Hello World!"); });
            });
        }
    }
}