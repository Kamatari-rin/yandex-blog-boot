const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    "/post",
    createProxyMiddleware({
      target: "http://tomcat-post-service:8082",
      changeOrigin: true,
    })
  );

  app.use(
    "/user",
    createProxyMiddleware({
      target: "http://tomcat-user-service:8081",
      changeOrigin: true,
    })
  );
};
