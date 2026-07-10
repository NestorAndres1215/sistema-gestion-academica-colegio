export interface SearchResultItem {
  id: string;
  name: string;
  subtitle?: string;
  avatar?: string | null;
  role?: string;
  status?: string;
  extraInfo?: string;
}