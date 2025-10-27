import { Post } from "../models/post.model"

export interface PostsResponse{
    posts: Array<Post>,
    size: Number
}