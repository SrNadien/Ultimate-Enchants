# Ultimate Enchants — estado

Implementado (JSON + lógica Java):
Soulbound, Magic Protection (data-driven), Displacement, Flaming/Chilling Rebuke,
Vitality, Bulwark, Reach (vía ItemAttributeModifierEvent), Air Affinity (parcial),
Gourmand, Insight, Cavalier, Ender Disruption, Frost Aspect, Leech, Instigating,
Magic Edge, Outlaw, Vigilante, Vorpal, Angler's Bounty, Pilfering, Phalanx,
overrides de Frost Walker (horse armor + lava, config-gated).

Solo JSON (sin lógica custom todavía, no revientan pero no hacen nada especial aún):
- excavating (falta radius mining real, solo hook de break speed)
- hunters_bounty (falta hook de loot en LivingDropsEvent para animales)
- quick_draw, trueshot, volley (requieren mixins/eventos de arco no cubiertos)
- curse_of_foolishness (falta bloquear XpChange), curse_of_mercy (falta cancelar golpe letal)

Mending override (config `mendingOverride`) NO está implementado — reemplazar Mending
por una "Preservation" real requiere datapack condicional o mixin, no lo armé en esta pasada.

No toqué build.gradle ni settings.gradle como pediste.
