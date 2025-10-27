export interface Post {
  id: number;
  authorId: number;
  authorName: string;
  title: string;
  summary: string;
  isLive: boolean;
  shares: number;
  likes: number;
  createdAt: string;
  updatedAt: string;
  content: string;
  imageUrl: string;
}