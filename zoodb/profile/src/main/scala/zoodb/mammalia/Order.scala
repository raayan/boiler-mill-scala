package zoodb.mammalia

import zoodb.Helpers.{DbEnum, DbEnumHelper}

sealed trait Order extends DbEnum

object Order extends DbEnumHelper[Order] {
  override val values: Set[Order] = Set(
    Rodentia,
    Chiroptera,
    Soricomorpha,
    Primates,
    Carnivora,
    Artiodactyla,
    Diprotodontia,
    Lagomorpha,
    Didelphimorphia,
    Cetacea,
    Dasyuromorphia,
    Afrosoricida,
    Erinaceomorpha,
    Cingulata,
    Peramelemorphia,
    Scandentia,
    Perissodactyla,
    Macroscelidea,
    Pilosa,
    Monotremata,
    Proboscidea,
  )

  case object Rodentia extends Order

  case object Chiroptera extends Order

  case object Soricomorpha extends Order

  case object Primates extends Order

  case object Carnivora extends Order

  case object Artiodactyla extends Order

  case object Diprotodontia extends Order

  case object Lagomorpha extends Order

  case object Didelphimorphia extends Order

  case object Cetacea extends Order

  case object Dasyuromorphia extends Order

  case object Afrosoricida extends Order

  case object Erinaceomorpha extends Order

  case object Cingulata extends Order

  case object Peramelemorphia extends Order

  case object Scandentia extends Order

  case object Perissodactyla extends Order

  case object Macroscelidea extends Order

  case object Pilosa extends Order

  case object Monotremata extends Order

  case object Proboscidea extends Order

}
