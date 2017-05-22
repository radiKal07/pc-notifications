/**
 * Server IP acts as a primary key.
 */
export class SettingsDao {
    constructor(args) {
        this.store = args.settingsStore;
    }

    async savePort(serverIp, port) {
        console.log('saving port: ', port);
        await this.store.update({serverIp: serverIp}, {$set: {port: port}}, {upsert: true});
    }

    async saveClientIp(serverIp, clientIp) {
        console.log('saving client ip: ', clientIp);
        await this.store.update({serverIp: serverIp}, {$set: {clientIp: clientIp}}, {upsert: true});
    }

    async getPort(serverIp) {
        let settings = await this.getSettings(serverIp);
        return settings.port;
    }

    async getClientIp(serverIp) {
        let settings = await this.getSettings(serverIp);
        return settings.clientIp;
    }

    async saveSettings(settings) {
        await this.store.update({serverIp: settings.serverIp}, {$set: {serverIp: settings.serverIp, clientIp: settings.clientIp, port: settings.port}}, {upsert: true});
    }

    async getSettings(serverIp) {
        let settings = await this.store.find({serverIp: serverIp});
        return settings;
    }
}