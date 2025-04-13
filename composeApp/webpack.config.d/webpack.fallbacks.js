config.resolve = {
    ...config.resolve,
    fallback: {
        "path": false,
        "fs": false,
        "os": false,
        "util": false
    }
};