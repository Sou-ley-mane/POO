export interface LoginRequest {
  telephone: string;
  pin: string;
}

export interface LoginResponse {
  token: string;
  userId: number;
  gieId: number | null;
  nom: string;
  prenom: string;
  role: 'SUPER_ADMIN' | 'ADMIN_GIE' | 'GESTIONNAIRE' | 'CHAUFFEUR';
  doitChangerPin: boolean;
}

export interface GieDto {
  id: number;
  nom: string;
  sigle: string;
  logoUrl: string | null;
  couleurPrimaire: string;
  couleurSecondaire: string;
  couleurAccent: string;
  adresse: string | null;
  telephone: string | null;
  email: string | null;
  statut: string;
}

export interface CreateGieRequest {
  nom: string;
  sigle: string;
  adresse?: string;
  telephone?: string;
  email?: string;
  telephoneAdmin: string;
  pinInitialAdmin: string;
  nomAdmin: string;
  prenomAdmin: string;
}

export interface VehiculeDto {
  id: number;
  immatriculation: string;
  marque: string | null;
  modele: string | null;
  annee: number | null;
  couleur: string | null;
  statut: 'EN_CIRCULATION' | 'EN_PANNE' | 'VENDU' | 'RETIRE';
  chauffeurActuelId: number | null;
  chauffeurActuelNomComplet: string | null;
  dateMiseEnService: string | null;
}

export interface ChauffeurDto {
  id: number;
  nom: string;
  prenom: string;
  telephone: string | null;
  numeroPermis: string | null;
  numeroCni: string | null;
  dateInscription: string;
  statut: 'ACTIF' | 'SUSPENDU' | 'RADIE';
  vehiculeAffecteId: number | null;
  vehiculeAffecteImmatriculation: string | null;
  periodiciteVersement: 'HEBDOMADAIRE' | 'BIMENSUELLE' | 'TRIMENSUELLE' | 'MENSUELLE';
  montantAttenduParPeriode: number;
}

export interface TypeFraisDto {
  id: number;
  libelle: string;
  montant: number;
  periodicite: string;
  actif: boolean;
}

export interface VersementGrilleLigneDto {
  versementId: number;
  vehiculeImmatriculation: string;
  chauffeurNomComplet: string;
  montantParTypeFrais: Record<string, number>;
  montantAttendu: number;
  montantVerse: number;
  reste: number;
  statut: 'SOLDE' | 'PARTIEL' | 'IMPAYE';
}

export interface SaisieVersementRequest {
  chauffeurId: number;
  periodeDebut?: string | null;
  montantVerse: number;
  modePaiement?: 'ESPECES' | 'MOBILE_MONEY' | 'VIREMENT' | null;
  fraisAppliquesIds?: number[] | null;
}

export interface UtilisateurDto {
  id: number;
  nom: string;
  prenom: string;
  telephone: string;
  email: string | null;
  role: 'ADMIN_GIE' | 'GESTIONNAIRE';
  statut: string;
  doitChangerPin: boolean;
}

export interface CreateGestionnaireRequest {
  nom: string;
  prenom: string;
  telephone: string;
  email?: string;
  pinInitial: string;
}

export interface DashboardStatsDto {
  totalCollecteJour: number;
  totalCollecteSemaine: number;
  totalCollecteMois: number;
  totalRestesCumules: number;
  nombreVehiculesActifs: number;
  nombreChauffeursActifs: number;
  topRetards: {
    chauffeurId: number;
    chauffeurNomComplet: string;
    reste: number;
    nombrePeriodesEnRetard: number;
  }[];
}
