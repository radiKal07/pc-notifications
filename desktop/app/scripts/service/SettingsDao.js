export class SettingsDao {
    constructor(args) {
        this.store = args.settingsStore;
    }

    async savePort(port) {
        console.log('saving port: ', port);
        await this.store.update({name: 'port'}, {$set: {value: port}}, {upsert: true});
    }

    async saveClientIp(clientIp) {
        console.log('saving client ip: ', clientIp);
        await this.store.update({name: 'clientIp'}, {$set: {value: clientIp}}, {upsert: true});
    }

    async getPort() {
        let portSettings = await this.store.find({name: 'port'});
        if (portSettings && portSettings.length > 0) {
            return portSettings[0].value;
        }
        return null;
    }

    async getClientIp() {
        let clientIp = await this.store.find({name: 'clientIp'});
        if (clientIp && clientIp.length > 0) {
            return clientIp[0].value;
        }
        return null;
    }
}