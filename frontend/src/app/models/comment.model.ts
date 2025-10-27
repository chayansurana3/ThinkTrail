export interface Comment {
  id: number;
  postId: number;
  userId: number;
  authorName: string;
  content: string;
  createdAt: Date;
  likes: number;
  avatar: number;
}