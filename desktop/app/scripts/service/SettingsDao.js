export class SettingsDao {
    constructor(args) {
        this.store = args.settingsStore;
    }

    async save(settings) {
        console.log('saving settings: ', settings);
        let prev = await this.getSettings();
        await this.store.remove({});
        let newSettings;
        if (settings.port) {
            newSettings = Object.assign({}, prev, {
                port: settings.port
            });
        }
        await this.store.insert(newSettings)
    }

    async getSettings() {
        let settings = await this.store.find({});
        console.log('found settings: ', settings);
        if (settings) {
            return settings[0];
        }
        return null;
    }

    async getPort() {
        let settings = await this.getSettings();
        return settings.port;
    }
}