import { Post } from "../models/post.model";

export interface createPostResponse{
    post: Post;
    success: boolean
}