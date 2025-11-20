# Display Unit Layout System

## Overview
The supermarket simulation uses a JSON-based layout system to store DisplayUnit positions. This ensures everyone on the team gets the same store layout when they clone/pull the repository.

## Files

### `display_layout.json` (Git-tracked)
- **Format**: JSON (human-readable, Git-friendly)
- **Purpose**: Stores the positions and types of all DisplayUnits in the simulation
- **Location**: `SupermarketSimulation/display_layout.json`
- **Tracked by Git**: ✅ Yes - This file is committed to the repository

### Structure
```json
[
  {
    "displayUnitType": "Fridge",
    "x": 75,
    "y": 225
  },
  {
    "displayUnitType": "SnackShelf",
    "x": 175,
    "y": 240
  }
]
```

## How It Works

### In the Editor (SettingWorld)
1. Open the editor via OPTIONS button from the landing page
2. Place, move, or delete DisplayUnits
3. Click **SAVE** to write changes to `display_layout.json`

### In the Simulation (SimulationWorld)
1. On startup, `SimulationWorld` loads display units from `display_layout.json`
2. If the file doesn't exist, it uses the default hardcoded layout
3. Each DisplayUnit computes its customer node dynamically based on its position
4. Customers navigate to these dynamic nodes

### Version Control Workflow

#### Making Layout Changes
```bash
# 1. Edit layout in the editor
# 2. Save the layout (creates/updates display_layout.json)
# 3. Commit the changes
git add display_layout.json
git commit -m "Update store layout: moved Fridge to new position"
git push
```

#### Getting Team Changes
```bash
# Pull latest changes from the repository
git pull

# The new layout will automatically load next time you run the simulation
```

## Important Notes

- **Old Format**: The old binary `.dat` file is ignored by Git and will be phased out
- **Default Layout**: If `display_layout.json` is missing, the simulation falls back to hardcoded positions in `SimulationWorld.createDefaultLayout()`
- **Manual Editing**: You can manually edit `display_layout.json` if needed (follow the JSON structure exactly)

## Available DisplayUnit Types
- `Fridge` (for drinks: Coke, Sprite, Fanta, Water)
- `SnackShelf` (for snacks: Doritos, Lays, Ruffles)
- `AppleBin`
- `OrangeBin`
- `CarrotBin`
- `LettuceBin`
- `SteakWarmer`
- `RawBeefHangers`

## Troubleshooting

### Layout not loading?
- Check that `display_layout.json` exists
- Verify JSON syntax is valid (no missing commas, brackets)
- Pull latest changes: `git pull`

### Layout different than expected?
- Make sure you've pulled the latest changes
- Check that your local `display_layout.json` matches the repository version
- If needed, discard local changes: `git checkout display_layout.json`
