const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        port: 3000,
        proxy: {
            "/api": {
                target: process.env.VUE_APP_API_URL,
                pathRewrite: {':3000' : ''},
                ws: true,
                changeOrigin: true,
                // // eslint-disable-next-line no-unused-vars
                // onProxyReq(proxyReq, req, res) {
                //     console.log('Proxying request with headers:', proxyReq.getHeaders());

                // },
            }
        }
    }
})

