---
navigation:
    title: Laser
    icon: 'dimresources:laser'
    parent: index.md
    position: 5
item_ids:
    - 'dimresources:laser'
---

# Laser

## Laser Set Up

The Interdimensional Laser is used to mine blocks from other planets. The laser must be placed on a structure made up of Dimensional Stone and Dimensional Stone Bricks. Example shown below

<GameScene zoom="1" interactive={true}>
  <ImportStructure src="assets/structures/laser.nbt" />
</GameScene>

The Laser requires a Planet Locator inside the GUI to know which planet to mine from

Some Planets require a higher level of the Laser Multiblock. Planet levels can range between 1 and 12. For each level the multiblock needs to be a block taller

<GameScene zoom="1" interactive={true}>
  <ImportStructure src="assets/structures/lasermax.nbt" />
</GameScene>
