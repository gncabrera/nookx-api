export interface ICurrency {
  id: number;
  code?: string | null;
  name?: string | null;
  symbol?: string | null;
}

export type NewCurrency = Omit<ICurrency, 'id'> & { id: null };
