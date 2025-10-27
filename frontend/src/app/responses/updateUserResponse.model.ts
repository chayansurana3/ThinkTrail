import { User } from "../models/user.model"

export interface updateUserResponse{
    userDTO: User
    success: boolean
}