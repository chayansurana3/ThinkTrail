export interface User {
  id: number;
  firstName: string;
  middleName?: string;
  lastName?: string;
  email: string;
  role: string;
  bio?: string;
  gender?: string;
  age?: number;
  postIds?: Array<Number>;
  commentIds?: Array<Number>;
  likeIds?: Array<Number>;
  followerIds?: Array<Number>;
  followingIds?: Array<Number>;
  avatar?: Number;
}