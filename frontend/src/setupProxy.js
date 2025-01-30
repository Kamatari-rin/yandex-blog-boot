const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
    app.use(
        "/api/posts",
        createProxyMiddleware({
            target: "http://tomcat-post-service:8082",
            changeOrigin: true,
            onProxyReq: (proxyReq, req, res) => {
                console.log(">>> Проксируем запрос на:", req.url);
            },
        })
    );

    app.use(
        "/api/users",
        createProxyMiddleware({
            target: "http://tomcat-user-service:8081",
            changeOrigin: true,
        })
    );

    app.use(
        "/api/likes",
        createProxyMiddleware({
            target: "http://tomcat-user-service:8081",
            changeOrigin: true,
        })
    );
};
