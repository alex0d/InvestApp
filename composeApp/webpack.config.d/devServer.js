config.devServer = {
    ...config.devServer,
    allowedHosts: 'all',
    host: '0.0.0.0',
    headers: {
        'Access-Control-Allow-Origin': '*'
    }
};