"use strict";
const schematic_command_1 = require("@angular/cli/models/schematic-command");
const project_1 = require("@angular/cli/utilities/project");
const schematicsProvider = (async function () {
    let workspace = await project_1.getWorkspaceDetails();
    let command = new schematic_command_1.SchematicCommand({ workspace: workspace }, null, null);
    let { listSchematicNames } = (await command.createWorkflow({ interactive: false })).engineHost;
    let defaultSchematicCollection = await command.getDefaultSchematicCollection();
    return {
        getCollection(collectionName) {
            return command.getCollection(collectionName);
        },
        listSchematics(collection) {
            return listSchematicNames(collection.description);
        },
        getSchematic(collection, schematicName, allowPrivate) {
            return command.getSchematic(collection, schematicName, allowPrivate);
        },
        getDefaultSchematicCollection() {
            return defaultSchematicCollection;
        }
    };
})();
module.exports = schematicsProvider;
