export class SettingsDao {
    constructor(args) {
        this.store = args.settingsStore;
    }

    async save(settings) {
        console.log('saving: ', settings);
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
        if (settings) {
            return settings[0];
        }
        return null;
    }

    async getPort() {
        return await this.getSettings().port;
    }
}